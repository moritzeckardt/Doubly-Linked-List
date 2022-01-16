package com.company;

import java.util.NoSuchElementException;

public class SortedSet implements OrderedSet {
    // Properties
    private ListItem head = null;
    private ListItem tail = null;

    // Inner classes
    private static class ListItem {
        // Properties
        ListItem previous;
        ListItem next;
        int value;

        // Constructor
        public ListItem(int value) {
            this.value = value;
        }

        // Methods
        public String toString(){
            if (next != null) {
                return "[" + value + "] --> ";
            }
            return "[" + value + "]";
        }

        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (!(obj instanceof ListItem))
                return false;

            ListItem li = (ListItem) obj;

            return this.value == li.value;
        }
    }

    // Methods
    @Override
    public void clear() {
        head = tail = null;
    }

    @Override
    public int size() {
        int size = 0; // Starting from zero
        for (ListItem li = head; li != null; li = li.next) {
            size++;
        }
        return size;
    }

    @Override
    public boolean contains(int value) {
        for (ListItem li = head; li != null; li = li.next) {
            if (li.value == value) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int[] toArray() {
        // List to array
        int counter = 0;
        int[] sortedArray = new int[size()];
        for (ListItem li = head; li != null; li = li.next) {
            sortedArray[counter] = li.value;
            counter++;
        }

        // Sort array using selection sort (not necessary tbh)
        int tmp;
        for (int i = 0; i < sortedArray.length; i++) {
            for (int k = 0; k < sortedArray.length; k++) {
                if (sortedArray[i] < sortedArray[k]) {
                    tmp = sortedArray[i];
                    sortedArray[i] = sortedArray[k];
                    sortedArray[k] = tmp;
                }
            }
        }

        return sortedArray;
    }

    @Override
    public int[] toReversedArray() {
        int[] arrToBeReversed = toArray();
        int arrLength = arrToBeReversed.length;

        // Reverse array without using an extra array
        int tmp;
        for (int i = 0; i < arrLength / 2; i++) {
            tmp = arrToBeReversed[i];
            arrToBeReversed[i] = arrToBeReversed[arrLength - i - 1];
            arrToBeReversed[arrLength - i - 1] = tmp;
        }

        return arrToBeReversed;
    }

    @Override
    public void add(int value) throws ElementExistsException {
        // Add element
        ListItem newLi = new ListItem(value);

        // If new element is the first one in the list
        if (head == null) {
            head = tail = newLi;
            newLi.previous = null;
            newLi.next = null;
        }
        else {
            for (ListItem li = head; li != null; li = li.next) {
                if (newLi.value == li.value) {
                    throw new ElementExistsException("Value already exists.");
                }
                else if (newLi.value < li.value) {
                    // Correct position in list was found, add it there
                    if (li.previous == null) {
                        // Element had no previous, so it must be the first
                        head = newLi;
                    } else {
                        li.previous.next = newLi;
                    }

                    // Assign pointers of new element
                    newLi.previous = li.previous;
                    newLi.next = li;

                    li.previous = newLi;
                    return;
                }
            }

            // New value is greater than all elements in list, add to end
            newLi.previous = tail;
            newLi.next = null;
            tail.next = newLi;
            tail = newLi;
        }
    }

    @Override
    public void add(int[] values) throws ElementExistsException {
        // Add elements by looping through the whole array
        for (int value : values) {
            add(value);
        }
    }

    @Override
    public void remove(int value) throws NoSuchElementException {
        for (ListItem li = head; li != null; li = li.next) {
            if (li.value == value) {
                // Change pointers of the previous and next element
                if (li.previous == null) {
                    head = li.next;
                } else {
                    li.previous.next = li.next;
                }

                if (li.next == null) {
                    tail = li.previous;
                } else {
                    li.next.previous = li.previous;
                }
                return;
            }
        }

        // List does not contain element
        throw new NoSuchElementException("Element does not exist.");
    }

    @Override
    public OrderedSet clone() {
        // Clone set
        OrderedSet copy = new SortedSet();
        copy.add(toArray());

        return copy;
    }

    @Override
    public OrderedSet getSetInBetween(int from, int to) {
        OrderedSet newSet = new SortedSet();

        // Get new set
        for (int j : toArray()) {
            if (j >= from && j <= to) {
                newSet.add(j);
            }
        }

        return newSet;
    }

    private OrderedSet compareTo(OrderedSet set, boolean intersectMode) {
        OrderedSet newSet = new SortedSet();

        // Get both sets to be compared as arrays
        int[] firstArray = toArray();
        int[] secondArray = set.toArray();

        // Compare sets
        int indexOfSecondArray = 0;
        for (int value : firstArray) {
            while (indexOfSecondArray < secondArray.length - 1 && secondArray[indexOfSecondArray] < value) {
                indexOfSecondArray++;
            }

            if ((value == secondArray[indexOfSecondArray]) == intersectMode) {
                newSet.add(value);
            }
        }

        return newSet;
    }
    @Override
    public OrderedSet intersect(OrderedSet set) {
        // Call help method
        return compareTo(set, true);
    }

    @Override
    public OrderedSet unite(OrderedSet set) {
        OrderedSet newSet = new SortedSet();

        // Add values from current and new set
        newSet.add(toArray());

        for (int val : set.toArray()) {
            try {
                newSet.add(val);
            } catch (ElementExistsException ignored) {
                // Irrelevant if thrown, just ignore
            }
        }

        return newSet;
    }

    @Override
    public OrderedSet subtract(OrderedSet set) {
        // Call help method
        return compareTo(set, false);
    }

    public String toString() {
        StringBuilder setAsString = new StringBuilder();
        for (ListItem li = head; li != null; li = li.next) {
            setAsString.append(li);
        }

        return setAsString.toString();
    }

    // Main
    public static void main(String[] args) {
        
    }
}

