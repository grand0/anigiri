package tech.bnuuy.anigiri.core.network.datasource.enumeration

enum class CollectionType(val value: String) {
    PLANNED("PLANNED"),
    WATCHED("WATCHED"),
    WATCHING("WATCHING"),
    POSTPONED("POSTPONED"),
    ABANDONED("ABANDONED");

    companion object {
        fun byValue(value: String): CollectionType? {
            return entries.firstOrNull { it.value == value }
        }
    }
}
