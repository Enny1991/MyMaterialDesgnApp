package com.eneaceolini.mymaterialdesignapp;

import android.provider.BaseColumns;

/**
 * Created by Enea on 03/07/15.
 */
public class TalksUsersDatabase {


    public TalksUsersDatabase() {}

    public static final class Talks implements BaseColumns {
        private Talks() {}
        public static final String TALKS_TABLE_NAME = "table_talks";
        public static final String TALK_URL = "talk_url";
        public static final String TALK_SERIES = "talk_series";
        public static final String TALK_SPEAKER_NAME = "talk_speaker_name";
        public static final String TALK_SPEAKER_FROM = "talk_speaker_from";
        public static final String TALK_TITLE = "talk_title";
        public static final String TALK_DATE = "talk_date";
        public static final String TALK_ABSTRACT = "talk_abstract";
        public static final String TALK_UID = "talk_uid";
        public static final String DEFAULT_SORT_ORDER = "talk_date ASC";
    }

    public static final class Users implements BaseColumns{
        private Users(){}

        public static final String USERS_TABLE_NAME = "table_users";
        public static final String USER_EMAIL = "user_email";
        public static final String USER_PASSWORD = "user_password";
        public static final String USER_LANGUAGE = "user_language";
        public static final String USER_LOCATION = "user_location";
    }

    public static final class TagType implements BaseColumns{
        private TagType() {}

        public static final String TAGTYPE_TABLE_NAME = "table_tagtype";
        public static final String TAGTYPE_TAGTYPE = "tagtype_tagtype";
        public static final String TAGTYPE_USER_ID = "tagtype_user_id";

    }

    public static final class TalkUser implements BaseColumns{
        private TalkUser() {}

        public static final String TALKUSER_TABLE_NAME = "table_talkuser";
        public static final String TALKUSER_TALK_ID = "talkuser_talk_id";
        public static final String TALKUSER_USER_ID = "talkuser_user_id";

    }

}
