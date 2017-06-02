package net.xaviersala.repository

import net.xaviersala.model.Persona
import java.util.concurrent.atomic.AtomicInteger

/**
 * Implementacio del repositori a memoria.
 *
 * Created by xavier on 01/06/17.
 */
class PersonaRepositoriMemory: PersonaRepositori{

    val persones = hashMapOf(
            1 to Persona(id=1, nom="Xavier", cognom="Sala"),
            2 to Persona(id=2, nom="Frederic", cognom="Garcia"),
            3 to Persona(id=3, nom="Filomeno", cognom="Pi"),
            4 to Persona(id=4, nom="Joan", cognom="Perez"),
            5 to Persona(id=5, nom="Marcel", cognom="Ferrer"),
            6 to Persona(id=6, nom="Joan", cognom="Pi"),
            7 to Persona(id=7, nom="Esteve", cognom="Marti")
    )

    var generaId: AtomicInteger = AtomicInteger(persones.keys.maxBy { it}!!)

    override fun getPersona(id: Int): Persona {
        if (!persones.containsKey(id)) {
            throw PersonaException("No existeix")
        }
        return persones[id]!!
    }

    override fun getPersones(nom: String): List<Persona> {
       // return persones.values.filter { it.nom == nom }.toCollection()
        return persones.values.filter { it.nom == nom }.map { it }
    }

    override fun afegirPersona(nom: String, cognom: String): Boolean {
        val nouId = generaId.incrementAndGet();
        persones.put(nouId, Persona(id=nouId, nom=nom, cognom = cognom))
        return true
    }

    override fun afegirPersonaBody(persona: Persona?): Boolean {
        if (persona!= null) {
            return true
        }
        throw PersonaException("Falten dades per entrar una persona")
    }

    override fun eliminarPersona(id: Int): Boolean {
        if (persones.containsKey(id)) {
            persones.remove(id)
            return true;
        }
        return false
    }

}

