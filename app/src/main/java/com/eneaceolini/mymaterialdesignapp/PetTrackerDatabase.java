package com.eneaceolini.mymaterialdesignapp;

import android.provider.BaseColumns;

/**
 * Created by Enea on 02/07/15.
 */
public class PetTrackerDatabase {

    private PetTrackerDatabase() {}

    // Pets table
    public static final class Pets implements BaseColumns {
        private Pets() {}
        public static final String PETS_TABLE_NAME = "table_pets";
        public static final String PET_NAME = "pet_name";
        public static final String PET_TYPE_ID = "pet_type_id";
        public static final String DEFAULT_SORT_ORDER = "pet_name ASC";
    }

    // Pet Type table
    public static final class PetType implements BaseColumns {
        private PetType() {}
        public static final String PETTYPE_TABLE_NAME = "table_pettypes";
        public static final String PET_TYPE_NAME = "pet_type";
        public static final String DEFAULT_SORT_ORDER = "pet_type ASC";
    }

}
