package net.xaviersala.repository

import net.xaviersala.model.Persona
import java.sql.DriverManager
import java.sql.ResultSet

/**
 * Implementacio de la interficie PersonaRepositori en una base de dades MYSQL.
 *
 * Abans de posar-lo cal crear la taula 'Persones' en la base de dades
 *
 * Created by xavier on 02/06/17.
 */
class PersonaRepositoriJDBC(url: String, user: String, password:String) : PersonaRepositori {

    val client = DriverManager.getConnection(url, user, password)

    init {
        val statement = client.createStatement()
        statement.execute("""
          CREATE TABLE IF NOT EXISTS Persones
              (id INT AUTO_INCREMENT NOT NULL,
              nom VARCHAR(25) NOT NULL,
              cognom VARCHAR(25) NOT NULL,
              PRIMARY KEY (`id`))
          """)
    }

    /**
     * Retorna un ResultSet a partir d'una consulta SELECT.
     */
    private fun enviaSelect(select: String): ResultSet {
        val statement = client.createStatement()
        val resultat = statement.executeQuery(select)
        return resultat
    }

    /**
     * Envia una comanda a la base de dades INSERT, UPDATE, DELETE.
     */
    private fun enviaUpdate(comanda: String): Int {
        val statement = client.createStatement()
        val resultat = statement.executeUpdate(comanda)
        return resultat
    }

    private fun executaInsert(nom: String, cognom: String): Int {
        return enviaUpdate("INSERT INTO Persones (nom, cognom) VALUES('" + nom + "', '" + cognom + "')")
    }

    /**
     * Localitza una persona a partir del seu ID.
     */
    override fun getPersona(id: Int): Persona {
        val resultat = enviaSelect("SELECT id, nom, cognom FROM Persones WHERE id=" + id)
        if (!resultat.next()) {
            throw PersonaException("No existeix")
        }
        return Persona(resultat.getInt("id"), resultat.getString("nom"), resultat.getString("cognom"))
    }

    /**
     * Localitza totes les persones que tenen el mateix nom.
     */
    override fun getPersones(nom: String): List<Persona> {
        val retorn = arrayListOf<Persona>()

        val resultat = enviaSelect("SELECT id, nom, cognom FROM Persones WHERE nom='" + nom + "'")
        while(resultat.next()) {
            retorn.add(Persona(resultat.getInt("id"), resultat.getString("nom"), resultat.getString("cognom")))
        }
        return retorn
    }

    /**
     * Afegir una persona a partir del nom i cognom.
     */
    override fun afegirPersona(nom: String, cognom: String): Boolean {
        executaInsert(nom, cognom)
        return true
    }

    /**
     * Afegeix una persona que s'ha rebut en el cos del missatge.
     */
    override fun afegirPersonaBody(persona: Persona?): Boolean {
        if (persona != null) {
            executaInsert(persona.nom, persona.cognom)
            return true
        }
        throw PersonaException("Falten dades per entrar una persona")
    }

    /**
     * Elimina un usuari a partir de la seva ID
     */
    override fun eliminarPersona(id: Int): Boolean {
        return enviaUpdate("DELETE FROM Persones WHERE id="+id) != 0
    }

}