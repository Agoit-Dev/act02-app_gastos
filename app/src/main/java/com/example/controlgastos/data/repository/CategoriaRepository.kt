package com.example.controlgastos.data.repository

import com.example.controlgastos.data.model.Categoria
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class CategoriaRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val categoriaCollection = firestore.collection("categorias")

    fun agregarCategoria(categoria: Categoria, onResult: (Boolean, String?) -> Unit) {
        val id = UUID.randomUUID().toString()
        val categoriaConId = categoria.copy(id = id)

        categoriaCollection.document(id)
            .set(categoriaConId)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    fun obtenerCategorias(esIngreso: Boolean, onResult: (List<Categoria>?, String?) -> Unit) {
        categoriaCollection
            .whereEqualTo("esIngreso", esIngreso)
            .get()
            .addOnSuccessListener { result ->
                val categorias = result.toObjects(Categoria::class.java)
                onResult(categorias, null)
            }
            .addOnFailureListener { e -> onResult(null, e.message) }
    }

    fun eliminarCategoria(id: String, onResult: (Boolean, String?) -> Unit) {
        categoriaCollection.document(id)
            .delete()
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    fun actualizarCategoria(categoria: Categoria, onResult: (Boolean, String?) -> Unit) {
        if (categoria.id.isBlank()) {
            onResult(false, "ID de categoría vacío")
            return
        }

        categoriaCollection.document(categoria.id)
            .set(categoria)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }
}