package com.ryszka.imageRestApi.security;

public class DBQueryStatements {
    public static final String ZIPCODE_AND_CITIES_LIKE_ZIPCODE = "select zipcode, city " +
            "from regions_postals " +
            "where zipcode like :zipcode% AND region = :region";


    public static final String GET_ZIPCODES_BY_REGION = "select zipcode, city " +
            "from regions_postals " +
            "where region = :region";


    public static final String GET_REGIONS = "select distinct region " +
            "from regions_postals " +
            "inner join countries c on regions_postals.id_country = c.id " +
            "where c.name = :country";


    /*public static final String GET_FILE_LINK_QUERY =
            "select t.tag_id, t.tag, i.id, i.name, i.path, i.image_id, u.email, u.user_id " +
            "from tagged_image " +
            "inner join image i on tagged_image.id_image = i.id " +
            "inner join tags t on tagged_image.id_tag = t.id " +
            "inner join users u on i.id_user = u.id " +
            "where id_image in (select image.id " +
            "from image " +
            "inner join users u2 on image.id_user = u2.id " +
            "where u2.user_id = :userId) " +
            "group by t.tag_id, t.tag, i.id " +
            "order by i.id";*/

    public static final String INSERT_INTO_TAGGED_IMAGE =
            "insert into tagged_image(id_image, id_tag) " +
                    "values ((select image.id " +
                    "from image " +
                    "inner join users u on image.id_user = u.id " +
                    "where u.user_id = :userId " +
                    "AND image.image_id = :imageId), " +
                    "(select id from tags where tag_id = (:tagId)))";


    public static final String GET_FILE_LINK_QUERY =
            "select t.tag_id, t.tag, i.id, i.name, i.path, i.image_id, email, user_id , profile_img_path, username " +
                    "from users " +
                    "inner join image i on users.id = i.id_user " +
                    "left join tagged_image ti on i.id = ti.id_image " +
                    "left join tags t on ti.id_tag = t.id " +
                    "where user_id = :userId ";

    public static final String GET_ENTITY_LIKE_FIRST_NAME_AT = "select email, user_id " +
            "from users " +
            "where email like :firstNameAt%";

    public static final String GET_PRINCIPAL_FROM_SESSION_ID = "select PRINCIPAL_NAME " +
            "from spring_session " +
            "where SESSION_ID = :sessionId";

    public static final String GET_FILES_BY_TAG_NAME =
            "select name, path, image_id, t.tag, t.tag_id, u.email, u.user_id , u.profile_img_path, u.username " +
                    "from image " +
                    "inner join tagged_image ti on image.id = ti.id_image " +
                    "inner join tags t on ti.id_tag = t.id " +
                    "inner join users u on image.id_user = u.id " +
                    "where image.id in (" +
                    "select image.id " +
                    "from image " +
                    "inner join tagged_image ti on image.id = ti.id_image " +
                    "inner join tags t on ti.id_tag = t.id " +
                    "where t.tag like %:tagName%" +
                    ")";

    public static final String COUNT_QUERY =
            "select count(*) " +
                    "from (select name " +
                    "from image " +
                    "inner join tagged_image ti on image.id = ti.id_image " +
                    "inner join tags t on ti.id_tag = t.id " +
                    "inner join users u on image.id_user = u.id " +
                    "where image.id in (select image.id " +
                    "from image " +
                    "inner join tagged_image ti on image.id = ti.id_image " +
                    "inner join tags t on ti.id_tag = t.id " +
                    "where t.tag = :tagName) " +
                    "group by image_id) as result";

    public static final String likeEmail = "SELECT id, user_id, " +
            " first_name, last_name," +
            " email, username, profile_img_path," +
            " password, inactive," +
            " login_type, forgot_password_entity_token_id, id_address, account_verification_token, account_verified" +
            " FROM USERS WHERE email like %:email%";

    public static final String likeUserName = "SELECT id, user_id, " +
            " first_name, last_name," +
            " email, username, profile_img_path," +
            " password, inactive," +
            " login_type, forgot_password_entity_token_id, id_address, account_verification_token, account_verified" +
            " FROM USERS WHERE username like %:username%";
}
