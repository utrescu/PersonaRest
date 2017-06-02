package net.xaviersala

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import net.xaviersala.repository.PersonaException
import net.xaviersala.repository.PersonaRepositori
import net.xaviersala.repository.PersonaRepositoriJDBC
import net.xaviersala.repository.PersonaRepositoriMemory
import spark.Request
import spark.Spark.*


fun main(args: Array<String>) {

    val repositori: PersonaRepositori = PersonaRepositoriMemory()

    // Per fer servir el repositori cal crear la taula Persones a la base de dades.
    // val repositori: PersonaRepositori = PersonaRepositoriJDBC("jdbc:mysql://localhost/persones", "root", "ies2010")

    fun Request.qp(key: String): String = this.queryParams(key)

    staticFiles.location("/public");

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
         * Afegeix una persona al repositori amb paràmetres 'nom' i 'cognom'
         */
        post("/") { req, res ->
            res.status(201)
            repositori.afegirPersona(nom = req.qp("nom"), cognom = req.qp("cognom"))
            "{\"result\":\"OK\"}"
        }

        /**
        * Afegeix una persona a partir del JSON que s'ha rebut en el
        * cos del missatge
        */
        post("/add") { req, res ->

            try {
                val p = repositori.afegirPersonaBody(
                        jacksonObjectMapper().readValue(req.body())
                )
                res.status(201)
                "{\"result\":\"OK\"}"
            } catch (e: PersonaException) {
                "{\"error\"=\"${e.message}\"}"
                res.status(404)
            } catch (e: MissingKotlinParameterException ) {
                "{\"error\"=\"Invalid Name\"}"
            } catch (e: NumberFormatException) {
                "{\"error\"=\"Invalid Value\"}"
            }
        }

        /**
         * Eliminar una persona del repositori.
         */
        delete("/:id") { req, res ->
            repositori.eliminarPersona(req.params("id").toInt())
            res.status(204)
            "{\"result\":\"OK\"}"
        }
    }

}

