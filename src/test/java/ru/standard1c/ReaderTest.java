package ru.standard1c;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import ru.standard1c.exception.NoEndOfSection;
import ru.standard1c.exception.NoStartOfSection;
import ru.standard1c.exception.UnknownAttribute;
import ru.standard1c.reader.source.AttributeSource;
import ru.standard1c.reader.DefaultReader;
import ru.standard1c.reader.Reader;
import ru.standard1c.reader.source.ScannerAttributeSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

/**
 * @author Maxim Tereshchenko
 */
class ReaderTest {

    private final Reader<TestCaptor, TestCaptor> reader = DefaultReader.from(
            "OpenSection",
            "CloseSection",
            TestCaptor::new
    );

    @Test
    void givenSectionWithoutName_thenNameIsNull() {
        assertThat(reader.read(attributeSource("OpenSection", "CloseSection")))
                .isEqualTo(new TestCaptor(null));
    }

    @Test
    void givenSectionWithName_thenNameIsExpected() {
        assertThat(reader.read(attributeSource("OpenSection=expected", "CloseSection")))
                .isEqualTo(new TestCaptor("expected"));
    }

    @Test
    void givenSectionWithEmptyName_thenNameIsEmpty() {
        assertThat(reader.read(attributeSource("OpenSection=", "CloseSection")))
                .isEqualTo(new TestCaptor(""));
    }

    @Test
    void givenAttributesBeforeSection_thenTheyShouldBeSkipped() {
        assertThat(
                reader.read(
                        attributeSource(
                                "ShouldBeSkipped=whatever",
                                "OpenSection=expected",
                                "CloseSection"
                        )
                )
        )
                .isEqualTo(new TestCaptor("expected"));
    }

    @Test
    void givenNoAttributes_thenNoStartOfSectionException() {
        var attributeSource = attributeSource("");

        assertThatThrownBy(() -> reader.read(attributeSource))
                .isInstanceOf(NoStartOfSection.class)
                .hasMessage("Could not find start of section attribute key 'OpenSection'");
    }

    @Test
    void givenNoEndOfSectionAttribute_thenNoEndOfSectionException() {
        var attributeSource = attributeSource("OpenSection");

        assertThatThrownBy(() -> reader.read(attributeSource))
                .isInstanceOf(NoEndOfSection.class)
                .hasMessage("Could not find end of section attribute key 'CloseSection'");
    }

    @Test
    void givenAttributeWithKey_thenHandleIt() {
        assertThat(
                reader.onAttribute("Key", (testCaptor, value) -> testCaptor.put("Key", value))
                        .read(
                                attributeSource(
                                        "OpenSection=expected",
                                        "Key=value",
                                        "CloseSection"
                                )
                        )
        )
                .isEqualTo(
                        new TestCaptor("expected")
                                .put("Key", "value")
                );
    }

    @Test
    void givenSeveralAttributes_thenHandleOnlySpecified() {
        assertThat(
                reader.onAttribute("Key1", (testCaptor, value) -> testCaptor.put("Key1", value))
                        .read(
                                attributeSource(
                                        "OpenSection=expected",
                                        "Key1=value1",
                                        "Key2=value2",
                                        "CloseSection"
                                )
                        )
        )
                .isEqualTo(
                        new TestCaptor("expected")
                                .put("Key1", "value1")
                );
    }

    @Test
    void givenSeveralAttributes_thenHandleAll() {
        assertThat(
                reader.onAttribute("Key1", (testCaptor, value) -> testCaptor.put("Key1", value))
                        .onAttribute("Key2", (testCaptor, value) -> testCaptor.put("Key2", value))
                        .read(
                                attributeSource(
                                        "OpenSection=expected",
                                        "Key1=value1",
                                        "Key2=value2",
                                        "CloseSection"
                                )
                        )
        )
                .isEqualTo(
                        new TestCaptor("expected")
                                .put("Key1", "value1")
                                .put("Key2", "value2")
                );
    }

    @Test
    void givenAttributesAfterEndOfSection_thenTheyShouldBeSkipped() {
        assertThat(
                reader.read(
                        attributeSource(
                                "OpenSection=expected",
                                "CloseSection",
                                "ShouldBeSkipped=whatever"
                        )
                )
        )
                .isEqualTo(new TestCaptor("expected"));
    }

    @Test
    void givenNestedSection_thenHandleIt() {
        assertThat(
                reader.onSection(
                                DefaultReader.from(
                                        "OpenNestedSection",
                                        "CloseNestedSection",
                                        TestCaptor::new
                                ),
                                TestCaptor::addNestedSection
                        )
                        .read(
                                attributeSource(
                                        "OpenSection=outer",
                                        "OpenNestedSection=nested",
                                        "CloseNestedSection",
                                        "CloseSection"
                                )
                        )
        )
                .isEqualTo(
                        new TestCaptor("outer")
                                .addNestedSection(new TestCaptor("nested"))
                );
    }

    @Test
    void givenNestedSectionWithAttribute_thenHandleNestedAttribute() {
        assertThat(
                reader.onSection(
                                DefaultReader.from(
                                                "OpenNestedSection",
                                                "CloseNestedSection",
                                                TestCaptor::new
                                        )
                                        .onAttribute("Key", (testCaptor, value) -> testCaptor.put("Key", value)),
                                TestCaptor::addNestedSection
                        )
                        .onAttribute("Key", (testCaptor, value) -> testCaptor.put("Key", value))
                        .read(
                                attributeSource(
                                        "OpenSection=outer",
                                        "Key=value",
                                        "OpenNestedSection=nested",
                                        "Key=value",
                                        "CloseNestedSection",
                                        "CloseSection"
                                )
                        )
        )
                .isEqualTo(
                        new TestCaptor("outer")
                                .put("Key", "value")
                                .addNestedSection(
                                        new TestCaptor("nested")
                                                .put("Key", "value")
                                )
                );
    }

    @Test
    void givenFinisher_thenReturnExpectedObject() {
        assertThat(
                reader.onEndOfSection(Optional::of)
                        .read(
                                attributeSource(
                                        "OpenSection",
                                        "CloseSection"
                                )
                        )
        )
                .isEqualTo(Optional.of(new TestCaptor(null)));
    }

    @Test
    void givenAnyAttribute_thenHandleAll() {
        assertThat(
                reader.onAnyOtherAttribute(
                                (testCaptor, attribute) -> testCaptor.put(
                                        attribute.key(),
                                        attribute.value()
                                )
                        )
                        .read(
                                attributeSource(
                                        "OpenSection",
                                        "Key1=value1",
                                        "Key2=value2",
                                        "CloseSection"
                                )
                        )
        )
                .isEqualTo(
                        new TestCaptor(null)
                                .put("Key1", "value1")
                                .put("Key2", "value2")
                );
    }

    @Test
    void givenFailOnUnknownAttribute_thenUnknownAttributeException() {
        var reader = this.reader.failOnUnknownAttribute();
        var attributeSource = attributeSource("OpenSection", "Key=value", "CloseSection");

        assertThatThrownBy(() -> reader.read(attributeSource))
                .isInstanceOf(UnknownAttribute.class)
                .hasMessage("Unknown attribute: Key=value");
    }

    private AttributeSource attributeSource(String... strings) {
        return new ScannerAttributeSource(new Scanner(String.join(System.lineSeparator(), strings)));
    }

    private static class TestCaptor {

        private final String name;
        private final Map<String, String> attributes = new HashMap<>();
        private final List<TestCaptor> nestedSections = new ArrayList<>();

        public TestCaptor(String name) {
            this.name = name;
        }

        public TestCaptor put(String key, String value) {
            attributes.put(key, value);

            return this;
        }

        public TestCaptor addNestedSection(TestCaptor nested) {
            nestedSections.add(nested);

            return this;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, attributes, nestedSections);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestCaptor that = (TestCaptor) o;
            return Objects.equals(name, that.name) &&
                    Objects.equals(attributes, that.attributes) &&
                    Objects.equals(nestedSections, that.nestedSections);
        }

        @Override
        public String toString() {
            return "TestCaptor{" +
                    "name='" + name + '\'' +
                    ", attributes=" + attributes +
                    ", nestedSections=" + nestedSections +
                    '}';
        }
    }
}
