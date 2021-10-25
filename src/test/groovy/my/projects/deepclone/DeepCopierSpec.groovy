package my.projects.deepclone

import org.junit.Test
import spock.lang.Specification;

class DeepCopierTest extends Specification {

    @Test
    def "should return the same string"() {
        given:
        def expectedString = "STRING"

        expect:
        DeepCopier.deepCopy(expectedString) == expectedString
    }

    @Test
    def "should return the same number"() {
        given:
        def expectedNumber = 123L

        expect:
        DeepCopier.deepCopy(expectedNumber) == expectedNumber
    }

    @Test
    def "should return the same enum"() {
        given:
        def expectedEnum = TestEnum.VALUE_ONE

        expect:
        DeepCopier.deepCopy(expectedEnum) == expectedEnum
    }

    @Test
    def "should return the copy of array"() {
        given:
        def sourceArray = ["abc", "def", "ghi"] as String[]

        when:
        def arrayCopy = DeepCopier.deepCopy(sourceArray)

        then:
        arrayCopy == sourceArray
        !arrayCopy.is(sourceArray)
    }

    @Test
    def "should return the copy of array of arrays"() {
        given:
        def sourceArray = [["abc", "def"], ["ghi"]] as String[][]

        when:
        def arrayCopy = DeepCopier.deepCopy(sourceArray)

        then:
        arrayCopy == sourceArray
        !arrayCopy.is(sourceArray)
    }

    @Test
    def "should copy collection with constructor by another collection"() {
        given:
        def sourceList = ["abc", "def", "ghi"]

        when:
        def listCopy = DeepCopier.deepCopy(sourceList)

        then:
        listCopy == sourceList
        !listCopy.is(sourceList)
    }

    @Test
    def "should copy collection with constructor by array"() {
        given:
        def sourceList = Arrays.asList("abc", "def", "ghi")

        when:
        def listCopy = DeepCopier.deepCopy(sourceList)

        then:
        listCopy == sourceList
        !listCopy.is(sourceList)
    }

    @Test
    def "should copy collection with noargs constructor"() {
        given:
        def sourceList = new NoArgsConstructorList()
        sourceList.addAll(["abc", "def", "ghi"])

        when:
        def listCopy = DeepCopier.deepCopy(sourceList)

        then:
        listCopy == sourceList
        !listCopy.is(sourceList)
    }

    @Test
    "should return deep copy of Man"() {
        given:
        def sourceMan = new Man("Mister First", 28, Arrays.asList("Knuth", "Schildt ", "Fowler"));

        when:
        Man copyMan = DeepCopier.deepCopy(sourceMan);

        then:
        sourceMan.properties == copyMan.properties
        !sourceMan.is(copyMan)
    }

    @Test
    "should copy recursive structure based on collection"() {
        given:
        def recursiveSource = new RecursiveStructure()
        recursiveSource.name = "NAME"
        recursiveSource.value = 123L
        recursiveSource.childrenList = [recursiveSource]

        when:
        def recursiveCopy = DeepCopier.deepCopy(recursiveSource)

        then:
        !recursiveCopy.is(recursiveSource)
        recursiveCopy.childrenList[0].is(recursiveCopy)
    }

    @Test
    "should copy recursive structure based on array"() {
        given:
        def recursiveSource = new RecursiveStructure()
        recursiveSource.name = "NAME"
        recursiveSource.value = 123L
        recursiveSource.childrenArray = [recursiveSource] as RecursiveStructure[]

        when:
        def recursiveCopy = DeepCopier.deepCopy(recursiveSource)

        then:
        !recursiveCopy.is(recursiveSource)
        recursiveCopy.childrenArray[0].is(recursiveCopy)
    }

    @Test
    "should copy recursive structure based on map"() {
        given:
        def recursiveSource = new RecursiveStructure()
        recursiveSource.name = "NAME"
        recursiveSource.value = 123L
        recursiveSource.childrenMap = ['CHILD': recursiveSource]

        when:
        def recursiveCopy = DeepCopier.deepCopy(recursiveSource)

        then:
        !recursiveCopy.is(recursiveSource)
        recursiveCopy.childrenMap['CHILD'].is(recursiveCopy)
    }

    @Test
    "should copy recursive structure based on nested object"() {
        given:
        def recursiveSource = new RecursiveStructure()
        recursiveSource.name = "NAME"
        recursiveSource.value = 123L
        recursiveSource.childObject = new NestedObject(recursiveSource)

        when:
        def recursiveCopy = DeepCopier.deepCopy(recursiveSource)

        then:
        !recursiveCopy.is(recursiveSource)
        recursiveCopy.childObject.recursiveStructure.is(recursiveCopy)
    }
}

class NoArgsConstructorList<T> extends ArrayList<T> {
}

class RecursiveStructure {
    String name;
    int value;
    List<RecursiveStructure> childrenList;
    RecursiveStructure[] childrenArray;
    Map<String, RecursiveStructure> childrenMap;
    NestedObject childObject;
}

class NestedObject {
    RecursiveStructure recursiveStructure;

    NestedObject(RecursiveStructure recursiveStructure) {
        this.recursiveStructure = recursiveStructure
    }
}

enum TestEnum {
    VALUE_ONE
}