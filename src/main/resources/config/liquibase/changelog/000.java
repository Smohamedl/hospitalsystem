<?xml version="1.0" encoding="utf-8"?>
<databaseChangeLog
    xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
    xmlns:ext="http://www.liquibase.org/xml/ns/dbchangelog-ext"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.6.xsd
                        http://www.liquibase.org/xml/ns/dbchangelog-ext http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd">

        <property name="autoIncrement" value="true"/>

    <!--
        JHipster core tables.
        The initial schema has the '00000000000001' id, so that it is over-written if we re-generate it.
    -->
    <changeSet id="00000000000001" author="jhipster">
        <createTable tableName="jhi_user">
            <column name="id" type="bigint" autoIncrement="${autoIncrement}">
                <constraints primaryKey="true" nullable="false"/>
            </column>
            <column name="login" type="varchar(50)">
                <constraints unique="true" nullable="false" uniqueConstraintName="ux_user_login"/>
            </column>
            <column name="password_hash" type="varchar(60)"/>
            <column name="first_name" type="varchar(50)"/>
            <column name="last_name" type="varchar(50)"/>
            <column name="email" type="varchar(191)">
                <constraints unique="true" nullable="true" uniqueConstraintName="ux_user_email"/>
            </column>
            <column name="image_url" type="varchar(256)"/>
            <column name="activated" type="boolean" valueBoolean="false">
                <constraints nullable="false" />
            </column>
            <column name="lang_key" type="varchar(10)"/>
            <column name="activation_key" type="varchar(20)"/>
            <column name="reset_key" type="varchar(20)"/>
            <column name="created_by" type="varchar(50)">
                <constraints nullable="false"/>
            </column>
            <column name="created_date" type="timestamp"/>
            <column name="reset_date" type="timestamp">
                <constraints nullable="true"/>
            </column>
            <column name="last_modified_by" type="varchar(50)"/>
            <column name="last_modified_date" type="timestamp"/>
        </createTable>

        <createTable tableName="jhi_authority">
            <column name="name" type="varchar(50)">
                <constraints primaryKey="true" nullable="false"/>
            </column>
        </createTable>

        <createTable tableName="jhi_user_authority">
            <column name="user_id" type="bigint">
                <constraints nullable="false"/>
            </column>
            <column name="authority_name" type="varchar(50)">
                <constraints nullable="false"/>
            </column>
        </createTable>

        <addPrimaryKey columnNames="user_id, authority_name" tableName="jhi_user_authority"/>

        <addForeignKeyConstraint baseColumnNames="authority_name"
                                 baseTableName="jhi_user_authority"
                                 constraintName="fk_authority_name"
                                 referencedColumnNames="name"
                                 referencedTableName="jhi_authority"/>

        <addForeignKeyConstraint baseColumnNames="user_id"
                                 baseTableName="jhi_user_authority"
                                 constraintName="fk_user_id"
                                 referencedColumnNames="id"
                                 referencedTableName="jhi_user"/>

        <addNotNullConstraint   columnName="password_hash"
                                columnDataType="varchar(60)"
                                tableName="jhi_user"/>
        <loadData
                  file="config/liquibase/data/user.csv"
                  separator=";"
                  tableName="jhi_user">
            <column name="activated" type="boolean"/>
            <column name="created_date" type="timestamp"/>
        </loadData>
        <dropDefaultValue tableName="jhi_user" columnName="created_date" columnDataType="datetime"/>
        <loadData
                  file="config/liquibase/data/authority.csv"
                  separator=";"
                  tableName="jhi_authority">
            <column name="name" type="string"/>
        </loadData>

        <loadData
                  file="config/liquibase/data/user_authority.csv"
                  separator=";"
                  tableName="jhi_user_authority">
            <column name="user_id" type="numeric"/>
        </loadData>
        <createTable tableName="jhi_persistent_audit_event">
            <column name="event_id" type="bigint" autoIncrement="${autoIncrement}">
                <constraints primaryKey="true" nullable="false"/>
            </column>
            <column name="principal" type="varchar(50)">
                <constraints nullable="false" />
            </column>
            <column name="event_date" type="timestamp"/>
            <column name="event_type" type="varchar(255)"/>
        </createTable>

        <createTable tableName="jhi_persistent_audit_evt_data">
            <column name="event_id" type="bigint">
                <constraints nullable="false"/>
            </column>
            <column name="name" type="varchar(150)">
                <constraints nullable="false"/>
            </column>
            <column name="value" type="varchar(255)"/>
        </createTable>
        <addPrimaryKey columnNames="event_id, name" tableName="jhi_persistent_audit_evt_data"/>

        <createIndex indexName="idx_persistent_audit_event"
                     tableName="jhi_persistent_audit_event"
                     unique="false">
            <column name="principal" type="varchar(50)"/>
            <column name="event_date" type="timestamp"/>
        </createIndex>

        <createIndex indexName="idx_persistent_audit_evt_data"
                     tableName="jhi_persistent_audit_evt_data"
                     unique="false">
            <column name="event_id" type="bigint"/>
        </createIndex>

        <addForeignKeyConstraint baseColumnNames="event_id"
                                 baseTableName="jhi_persistent_audit_evt_data"
                                 constraintName="fk_evt_pers_audit_evt_data"
                                 referencedColumnNames="event_id"
                                 referencedTableName="jhi_persistent_audit_event"/>
    </changeSet>

    <changeSet author="jhipster" id="00000000000002" context="test">
        <createTable tableName="jhi_date_time_wrapper">
            <column autoIncrement="${autoIncrement}" name="id" type="BIGINT">
                <constraints primaryKey="true" primaryKeyName="jhi_date_time_wrapperPK"/>
            </column>
            <column name="instant" type="timestamp"/>
            <column name="local_date_time" type="timestamp"/>
            <column name="offset_date_time" type="timestamp"/>
            <column name="zoned_date_time" type="timestamp"/>
            <column name="local_time" type="time"/>
            <column name="offset_time" type="time"/>
            <column name="local_date" type="date"/>
        </createTable>
    </changeSet>
</databaseChangeLog>