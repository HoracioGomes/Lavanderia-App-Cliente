package com.example.lavanderia_cliente.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class LavanderiaMigrations {


    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE pecaroupa ADD COLUMN cor TEXT")
        }

    }

    val MIGRATION_2_3: Migration = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Criar nova tabela com as informações desejadas
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS `PecaRoupa_novo` " +
                        "(`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "`nome` TEXT, " +
                        "`status` TEXT) "
            );

            // Copiar dados da tabela antiga para a nova
            database.execSQL(
                "INSERT INTO PecaRoupa_novo (id, nome, status) " +
                        "SELECT id, nome, status FROM PecaRoupa"
            );

            // Remove tabela antiga
            database.execSQL("DROP TABLE PecaRoupa");

            // Renomear a tabela nova com o nome da tabela antiga
            database.execSQL("ALTER TABLE PecaRoupa_novo RENAME TO PecaRoupa");
        }

    }


    val MIGRATIONS = arrayOf(MIGRATION_1_2, MIGRATION_2_3)

}