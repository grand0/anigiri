package tech.bnuuy.anigiri.core.network.datasource.enumeration

enum class CollectionType(val value: String) {
    PLANNED("PLANNED"),
    WATCHING("WATCHING"),
    WATCHED("WATCHED"),
    POSTPONED("POSTPONED"),
    ABANDONED("ABANDONED");

    companion object {
        fun byValue(value: String): CollectionType? {
            return entries.firstOrNull { it.value == value }
        }
    }
}
