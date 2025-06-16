package com.github.benshi.mybatis.flex.plugin.generator.impl;

import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Entity class for table: test_table
 *
 * <pre>
 * Test Tabl
 * 11111
 * </pre>
 */
@Table("test_table")
public class TestTable implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    /**
     * Gets the value of the id property.
     *
     * <pre>
     * Primary Key
     * sssss
     * saaaaa
     * </pre>
     *
     * @return the value of the property
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * <pre>
     * Primary Key
     * </pre>
     *
     * @param id the new value for the property
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the value of the id property wrapped in an Optional.
     *
     * <pre>
     * Primary Key
     * </pre>
     *
     * @return an Optional containing the value of the property, or empty if null
     */
    @Nonnull
    public Optional<Integer> getIdWithOptional() {
        return Optional.ofNullable(id);
    }

    /**
     * Gets the value of the name property.
     *
     * <pre>
     * Name Column
     * ass
     * asdasd
     * </pre>
     * 
     * @return the value of the property
     *
     */
    @Nullable
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * <pre>
    * {@code
     * Set<String> s;
     * System.out.println(s);
     * }
     * </pre>
     *
     * @param name the new value for the property
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the value of the name property wrapped in an Optional.
     *
     * <pre>
     * Name Column
     * </pre>
     *
     * @return an Optional containing the value of the property, or empty if null
     */
    @Nonnull
    public Optional<String> getNameWithOptional() {
        return Optional.ofNullable(name);
    }
}