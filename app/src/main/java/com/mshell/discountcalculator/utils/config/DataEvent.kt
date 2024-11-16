package com.mshell.discountcalculator.utils.config

enum class DataEvent {
    INSERT,
    UPDATE,
    DELETE;

    // This field will store the ID dynamically
    private var _id: Long? = null

    // Getter for the ID
    val id: Long
        get() = _id ?: throw IllegalStateException("ID must be set for UPDATE or DELETE")

    // Method to set the ID dynamically for UPDATE or DELETE
    fun withId(id: Long): DataEvent {
        _id = id
        return this
    }

    // Check if the event requires an ID (i.e., UPDATE or DELETE)
    val requiresId: Boolean
        get() = this == UPDATE || this == DELETE
}
