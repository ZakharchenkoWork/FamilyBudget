package com.faigenbloom.famillyspandings.comon

import java.util.SortedMap

class PlatesSorter<T : Countable>(
    private val patterns: List<Pattern<T>> =
        listOf(
            Pattern(
                listOf(
                    PlateSizeType.SIZE_THREE_BY_THREE,
                ),
            ),
            Pattern(
                listOf(
                    PlateSizeType.SIZE_THREE_BY_TWO,
                    PlateSizeType.SIZE_THREE_BY_ONE,
                ),
            ),
            Pattern(
                listOf(
                    PlateSizeType.SIZE_THREE_BY_TWO,
                    PlateSizeType.SIZE_TWO_BY_ONE,
                    PlateSizeType.SIZE_ONE_BY_ONE,
                ),
            ),
            Pattern(
                listOf(
                    PlateSizeType.SIZE_THREE_BY_TWO,
                ),
            ),
            Pattern(
                listOf(
                    PlateSizeType.SIZE_TWO_BY_TWO,
                    PlateSizeType.SIZE_THREE_BY_ONE,
                    PlateSizeType.SIZE_TWO_BY_ONE,
                ),
            ),
            Pattern(
                listOf(
                    PlateSizeType.SIZE_TWO_BY_TWO,
                    PlateSizeType.SIZE_THREE_BY_ONE,
                    PlateSizeType.SIZE_ONE_BY_ONE,
                    PlateSizeType.SIZE_ONE_BY_ONE,
                ),
            ),
            Pattern(
                listOf(
                    PlateSizeType.SIZE_TWO_BY_TWO,
                    PlateSizeType.SIZE_TWO_BY_ONE,
                ),
            ),
            Pattern(
                listOf(
                    PlateSizeType.SIZE_TWO_BY_TWO,
                    PlateSizeType.SIZE_ONE_BY_ONE,
                    PlateSizeType.SIZE_ONE_BY_ONE,
                ),
            ),
            Pattern(
                listOf(
                    PlateSizeType.SIZE_THREE_BY_ONE,
                    PlateSizeType.SIZE_THREE_BY_ONE,
                    PlateSizeType.SIZE_THREE_BY_ONE,
                ),
            ),
            Pattern(
                listOf(
                    PlateSizeType.SIZE_THREE_BY_ONE,
                    PlateSizeType.SIZE_THREE_BY_ONE,
                    PlateSizeType.SIZE_TWO_BY_ONE,
                    PlateSizeType.SIZE_ONE_BY_ONE,
                ),
            ),
            Pattern(
                listOf(
                    PlateSizeType.SIZE_THREE_BY_ONE,
                    PlateSizeType.SIZE_TWO_BY_ONE,
                    PlateSizeType.SIZE_TWO_BY_ONE,
                    PlateSizeType.SIZE_TWO_BY_ONE,
                ),
            ),
            Pattern(
                listOf(
                    PlateSizeType.SIZE_THREE_BY_ONE,
                    PlateSizeType.SIZE_TWO_BY_ONE,
                    PlateSizeType.SIZE_TWO_BY_ONE,
                    PlateSizeType.SIZE_ONE_BY_ONE,
                    PlateSizeType.SIZE_ONE_BY_ONE,
                ),
            ),
            Pattern(
                listOf(
                    PlateSizeType.SIZE_THREE_BY_ONE,
                    PlateSizeType.SIZE_TWO_BY_ONE,
                    PlateSizeType.SIZE_TWO_BY_ONE,
                    PlateSizeType.SIZE_ONE_BY_ONE,
                ),
            ),
            Pattern(
                listOf(
                    PlateSizeType.SIZE_THREE_BY_ONE,
                ),
            ),
            Pattern(
                listOf(
                    PlateSizeType.SIZE_TWO_BY_TWO,
                    PlateSizeType.SIZE_ONE_BY_ONE,
                ),
            ),
            Pattern(
                listOf(
                    PlateSizeType.SIZE_TWO_BY_ONE,
                    PlateSizeType.SIZE_TWO_BY_ONE,
                    PlateSizeType.SIZE_TWO_BY_ONE,
                ),
            ),
            Pattern(
                listOf(
                    PlateSizeType.SIZE_TWO_BY_ONE,
                    PlateSizeType.SIZE_TWO_BY_ONE,
                    PlateSizeType.SIZE_ONE_BY_ONE,
                    PlateSizeType.SIZE_ONE_BY_ONE,
                ),
            ),
            Pattern(
                listOf(
                    PlateSizeType.SIZE_TWO_BY_ONE,
                    PlateSizeType.SIZE_TWO_BY_ONE,
                    PlateSizeType.SIZE_ONE_BY_ONE,
                ),
            ),
            Pattern(
                listOf(
                    PlateSizeType.SIZE_TWO_BY_ONE,
                    PlateSizeType.SIZE_ONE_BY_ONE,
                ),
            ),
            Pattern(
                listOf(
                    PlateSizeType.SIZE_TWO_BY_ONE,
                ),
            ),
            Pattern(
                listOf(
                    PlateSizeType.SIZE_ONE_BY_ONE,
                    PlateSizeType.SIZE_ONE_BY_ONE,
                    PlateSizeType.SIZE_ONE_BY_ONE,
                ),
            ),
            Pattern(
                listOf(
                    PlateSizeType.SIZE_ONE_BY_ONE,
                    PlateSizeType.SIZE_ONE_BY_ONE,
                ),
            ),
            Pattern(
                listOf(
                    PlateSizeType.SIZE_ONE_BY_ONE,
                ),
            ),
        ),
) {
    fun prepareByDates(data: List<T>): List<List<T>> {
        return data.groupBy { it.getSortableDate() }.map {
            it.value
        }
    }

    fun preparePlatesSizes(data: List<T>): HashMap<T, PlateSizeType> {
        val result = HashMap<T, PlateSizeType>()
        val sortedData = data.sortedBy {
            it.getSortableValue()
        }
        if (sortedData.size == 1) {
            result[sortedData[0]] = PlateSizeType.SIZE_ONE_BY_ONE
        } else if (sortedData.size >= 2) {
            var lastValue: Long = sortedData.first().getSortableValue()
            var lastPlateSizeIndex = 0

            for (item in sortedData) {
                val value = item.getSortableValue()

                while (value >= lastValue * 1.5) {
                    lastPlateSizeIndex++
                    lastValue = value
                }
                result[item] = PlateSizeType.values()[lastPlateSizeIndex]
            }
        }
        return result
    }

    fun findPattern(items: HashMap<T, PlateSizeType>): List<Pattern<T>> {
        val foundPatterns = ArrayList<Pattern<T>>()
        val currentItems = items.toSortedMap { o1, o2 ->
            val result = (o2.getSortableValue() - o1.getSortableValue()).toInt()
            if (result == 0) {
                return@toSortedMap 1
            }
            return@toSortedMap result
        }
        for (pattern in patterns) {
            var foundPlates: List<T>?

            do {
                foundPlates = searchForPlates(currentItems, pattern.plates)
                foundPlates?.let { platesData ->
                    foundPatterns.add(
                        pattern.copy().apply {
                            this.items = platesData
                        },
                    )
                    platesData.forEach { foundKey ->
                        val iterator = currentItems.entries.iterator()
                        while (iterator.hasNext()) {
                            val next = iterator.next()
                            if (next.key == foundKey) {
                                iterator.remove()
                            }
                        }
                    }
                }
            } while (foundPlates != null)
        }

        return foundPatterns.toList()
    }

    private fun searchForPlates(
        items: SortedMap<T, PlateSizeType>,
        patternPlates: List<PlateSizeType>,
    ): List<T>? {
        val matches = ArrayList<T>()
        val currentItems = items.toSortedMap { _, _ -> return@toSortedMap 1 }
        for (plate in patternPlates) {
            findPair(currentItems, plate)?.let {
                matches.add(it)
                val iterator = currentItems.entries.iterator()
                while (iterator.hasNext()) {
                    val next = iterator.next()
                    if (next.key == it) {
                        iterator.remove()
                    }
                }
            } ?: run {
                return null
            }
        }
        return matches.toList()
    }

    private fun findPair(items: SortedMap<T, PlateSizeType>, plateSizeType: PlateSizeType): T? {
        for (pair in items) {
            if (pair.value == plateSizeType) {
                return pair.key
            }
        }
        return null
    }
}

enum class PlateSizeType {
    SIZE_ONE_BY_ONE,
    SIZE_TWO_BY_ONE,
    SIZE_THREE_BY_ONE,
    SIZE_TWO_BY_TWO,
    SIZE_THREE_BY_TWO,
    SIZE_THREE_BY_THREE,
}

data class Pattern<T>(val plates: List<PlateSizeType>) {
    var items: List<T> = emptyList()
}

interface Countable {
    fun getSortableValue(): Long
    fun getSortableDate(): Int
}
