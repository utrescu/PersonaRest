package net.xaviersala

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import net.xaviersala.repository.PersonaException
import net.xaviersala.repository.PersonaRepositori
import net.xaviersala.repository.PersonaRepositoriMemory
import spark.Request
import spark.Spark.*


fun main(args: Array<String>) {

    val repositori: PersonaRepositori = PersonaRepositoriMemory()

    fun Request.qp(key: String): String = this.queryParams(key)


    path ("/persones") {

        after("/*") { _, res ->
            res.type("application/json")
        }

        /**
         * Obtenir una persona a partir del seu ID.
         */
        get("/:id") { req, res ->
            try {
                jacksonObjectMapper().writeValueAsString(repositori.getPersona(req.params("id").toInt()))
            }catch (e: PersonaException) {
                res.status(404)
                "{\"error\"=\"${e.message}\"}"
            }
        }

        /**
         * Obtenir totes les persones que tenen un nom determinat.
         */
        get("/nom/:nom") { req, _ ->
            jacksonObjectMapper().writeValueAsString(repositori.getPersones(req.params("nom")))
        }

        /**
         * Afegeix una persona al repositori.
         */
        post("/") { req, res ->
            res.status(201)
            jacksonObjectMapper().writeValueAsString(
                    repositori.afegirPersona(nom = req.qp("nom"), cognom = req.qp("cognom")))
        }

        post("/add") { req, res ->

            try {
                repositori.afegirPersonaBody(
                        jacksonObjectMapper().readValue(req.body())
                )
                res.status(201)
            } catch (e: PersonaException) {
                "{\"error\"=\"${e.message}\"}"
                res.status(404)
            } catch (e: MissingKotlinParameterException) {
                "{\"error\"=\"Invalid Name\"}"
            }
        }

        /**
         * Eliminar una persona del repositori.
         */
        delete("/:id") { req, res ->
            repositori.eliminarPersona(req.params("id").toInt())
            res.status(204)
            "{resultat=ok}"
        }
    }

}

