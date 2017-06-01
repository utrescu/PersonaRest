package net.xaviersala.repository

import net.xaviersala.model.Persona

/**
 * Recuperacio de dades del repositori de Persones.
 *
 * Created by xavier on 01/06/17.
 */
interface PersonaRepositori {

    /**
     * Obtenir una persona a partir del seu ID
     * @param id: Id de la persona a cercar
     * @return Persona amb l'ID
     */
    fun getPersona(id: Int): Persona

    /**
     * Obtenir totes les persones que tenen un determinat nom.
     * @param nom: Nom a cercar
     * @return llista de persones
     */
    fun getPersones(nom: String): List<Persona>

    /**
     * Afegir una persona al repositori
     * @param  Afegir una persona al repositori de dades
     * @Return OK
     */
    fun afegirPersona(nom: String, cognom: String): Persona

    fun afegirPersonaBody(persona: Persona?): Persona

    /**
     * Esborrar una persona
     * @param id
     * @Return Ok
     */
    fun eliminarPersona(id: Int): Boolean

}