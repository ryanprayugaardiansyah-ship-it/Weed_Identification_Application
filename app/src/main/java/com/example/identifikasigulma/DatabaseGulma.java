package com.example.identifikasigulma;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseGulma extends SQLiteOpenHelper {

    public static final String COLUMN_IDENTIFIED_WEED = "identified_weed";
    public static final String COLUMN_DESCRIPTION_HERBICIDE = "deskripsi";
    private static final String DATABASE_NAME = "weeds.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_WEEDS = "weeds";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_WEED_NAME = "nama_gulma";
    public static final String COLUMN_SCIENTIFIC_NAME = "nama_ilmiah";
    public static final String COLUMN_DESCRIPTION = "deskripsi";
    public static final String COLUMN_IMAGE_URL = "gambar_gulma";

    private static final String TABLE_IDENTIFICATIONS = "identifications";
    private static final String COLUMN_IDENTIFICATION_ID = "_id";
    private static final String COLUMN_IDENTIFICATION_TIME = "identification_time";

    public static final String TABLE_HERBICIDE_RECOMMENDATIONS = "herbicide_recommendations";
    public static final String COLUMN_WEED_ID = "weed_id";
    public static final String COLUMN_CROP_TYPE = "crop_type";
    public static final String COLUMN_HERBICIDE_NAME = "herbicide_name";

    public static final String TABLE_ADDITIONAL_HERBICIDE_RECOMMENDATIONS = "additional_herbicide_recommendations";
    public static final String COLUMN_ADDITIONAL_ID = "_id";
    public static final String COLUMN_ADDITIONAL_CROP_TYPE = "crop_type";
    public static final String COLUMN_ADDITIONAL_HERBICIDE_NAME = "herbicide_name";
    public static final String COLUMN_ADDITIONAL_DESCRIPTION = "description";

    private static final String CREATE_TABLE_ADDITIONAL_HERBICIDE_RECOMMENDATIONS =
            "CREATE TABLE " + TABLE_ADDITIONAL_HERBICIDE_RECOMMENDATIONS + " (" +
                    COLUMN_ADDITIONAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ADDITIONAL_CROP_TYPE + " TEXT, " +
                    COLUMN_ADDITIONAL_HERBICIDE_NAME + " TEXT, " +
                    COLUMN_ADDITIONAL_DESCRIPTION + " TEXT, " +
                    "active_ingredient TEXT" + // Tambahkan kolom active_ingredient
                    ");";

    private static final String CREATE_TABLE_WEEDS =
            "CREATE TABLE " + TABLE_WEEDS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_WEED_NAME + " TEXT, " +
                    COLUMN_SCIENTIFIC_NAME + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_IMAGE_URL + " TEXT" +
                    ");";

    private static final String CREATE_TABLE_IDENTIFICATIONS =
            "CREATE TABLE " + TABLE_IDENTIFICATIONS + " (" +
                    COLUMN_IDENTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_IDENTIFIED_WEED + " TEXT, " +
                    COLUMN_IDENTIFICATION_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ");";


    private static final String CREATE_TABLE_HERBICIDE_RECOMMENDATIONS =
            "CREATE TABLE " + TABLE_HERBICIDE_RECOMMENDATIONS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_WEED_ID + " INTEGER, " +
                    COLUMN_CROP_TYPE + " TEXT, " +
                    COLUMN_HERBICIDE_NAME + " TEXT, " +
                    COLUMN_DESCRIPTION_HERBICIDE + " TEXT, " +
                    "active_ingredient TEXT, " + // Tambahkan kolom active_ingredient
                    "FOREIGN KEY (" + COLUMN_WEED_ID + ") REFERENCES " + TABLE_WEEDS + "(" + COLUMN_ID + ")" +
                    ");";


    public DatabaseGulma(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_WEEDS);
        db.execSQL(CREATE_TABLE_IDENTIFICATIONS);
        db.execSQL(CREATE_TABLE_HERBICIDE_RECOMMENDATIONS);
        db.execSQL(CREATE_TABLE_ADDITIONAL_HERBICIDE_RECOMMENDATIONS);
        addInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL(CREATE_TABLE_HERBICIDE_RECOMMENDATIONS);
        }
        if (oldVersion < 3) {
            db.execSQL(CREATE_TABLE_ADDITIONAL_HERBICIDE_RECOMMENDATIONS);  // Create the new table if not exists
        }
    }

    private void addInitialData(SQLiteDatabase db) {
        // Add initial weed data
        addWeed(db, "Alternanthera_Philoxeroides", "Alternanthera Philoxeroides", "Alternanthera Philoxeroides atau yang sering disebut sebagai gulma Aligator, adalah spesies asli daerah beriklim sedang di Amerika Selatan, yang meliputi Argentina, Brazil, Paraguay, dan Uruguay. Gulma ini dapat tumbuh subur di lingkungan kering dan perairan dan ditandai dengan bunga tipis berwarna keputihan di sepanjang tangkai, batang berongga tidak beraturan dan pola daun sederhana.", "alternanthera_philoxeroides");
        addWeed(db, "Cacabean", "Ludwigia Octovalvis", "Cacabean atau dalam bahasa latin nya disebut Ludwigia Octovalvis, merupakan salah satu gulma dengan tipe aquatic weed, gulma ini hampir bisa ditemui disemua wilayah seperti di Amerika, Australia, Afrika dan di Asia, termasuk di Indonesia, di Indonesia sendiri cacabean atau Ludwigia merupakan gulma pada tanaman padi. Cacabean atau Ludwigia mempunyai bunga yang berwarna kuning dengan mempunyai 4 kelopak bungan dengan masing-masing panjangnya 10 mm, kebutuhan cahaya untuk gulma ini bisa menyesuaikan diri, cacabean mampu tumbuh baik dengan cahaya yang banyak dan juga pada cahaya yang ternaungi.", "cacabean");
        addWeed(db, "Commelina_Diffusa", "Commelina Diffusa", "Commelina diffusa adalah tanaman tahunan atau semak yang tumbuh terutama di daerah tropis yang mengalami musim kering. Commelina diffusa memiliki daun hijau gelap yang mengkilap dan tumbuh merambat rendah. Tanaman ini lebih suka tumbuh di tempat teduh.", "commelina_diffusa");
        addWeed(db, "Kangkung_Sawah", "Ipomoea Aquatica", "Kangkung Sawah memiliki nama latin Ipomoea Aquatica adalah tanaman herba yang tumbuh di Asia yang seringkali digunakan untuk tumisan dan hidangan lainnya. Tanaman kangkung memiliki akar jenis tunggang dan memiliki banyak percabangan, batang kangkung berbentuk bulat, berlubang dan mengandung banyak air. Tumbuhan ini dapat menjadi ancaman ekoloogis karena merambat dan menaungi tanaman budidaya lainnya, yang menyebabkan tanaman tersebut kehilangan sinar matahari dan oksigen.", "kangkung_sawah");
        addWeed(db, "Kate_Mas", "Euphorbia Heterophylla", "Kate Mas memiliki nama latin Euphorbia Heterophylla. Kate Mas merupakan tanaman gulma yang tumbuh pada daerah lembab. Tanaman ini berasal dari Amerika Tengah dan Amerika Selatan. Penyebarannya meluas ke daerah tropis dan subtropis termasuk ke Indonesia. Diperkenalkan ke Asia Selatan dan Tenggara sebagai tanaman hias. Kate mas juga seringkali dianggap sebagai gulma, namun berkhasiat sebagai obat dan banyak digunakan dalam pengobatan tradisional Afrika. Daun nya mengandung senyawa dengan aktivitas diantaranya antimikrobia dan antiinflamasi.", "kate_mas");
        addWeed(db, "Kiambang", "Pistia Stratiotes", "Pistia Stratiotes atau yang sering disebut Kiambang merupakan salah satu tumbuhan air yang biasa dijumpai mengapung di perairan tenang seperti di lahan sawah, kolam, dan rawa-rawa. Kiambang juga dikategorikan sebagai gulma tanaman padi. Dengan menghisap unsur hara pada tanah sehingga tanaman padi akan bersaing untuk mendapatkan unsur hara yang kita berikan, masalah lain adalah kiambang berkembangbiak dengan cepat sehingga petani seringkali melakukan pembersihan secara berkala. Cara pengendalian lain yaitu dengan menggunakan herbisida.", "kiambang");
        addWeed(db, "Kucing_kucingan", "Acalypha Indica", "Acalypha indica merupakan salah satu gulma yang memiliki banyak nama daerah diantaranya kucing-kucingan, kucing galak, akar kucing, anting-anting, bayam kucing, dan masih banyak lagi. Hal ini dikarenakan, kucing sangat tertarik dengan akar pohon ini karena gulma ini berfungsi untuk melancarkan pencernaan kucing. Tanaman ini tersebar luas di wilayah tropis mulai dari Afrika Barat menuju India, Indo-China menuju Filipina dan Jawa. Acalypha Indica atau kucing-kucingan merupakan gulma yang umumnya tumbuh secara liar di pinggir jalan, lapangan rumput, lahan sawah, maupun di lereng bukit.", "kucing_kucingan");
        addWeed(db, "Maman_Lanang", "Cleome Rutidosperma", "Cleome Rutidosperma atau yang sering disebut Maman Lanang Merupakan salah satu tumbuhan gulma yang termasuk dalam salah satu anggota famili cleomaceae. Maman Lanang biasanya ditemukan di pinggir jalan, sawah, ladang dan perkebunan warga. Karena dapat berkembang biak dengan sangat cepat, Maman Lanang dianggap sebagai spesies invasif di sebagian besar negara.", "maman_lanang");
        addWeed(db, "Putri_Malu", "Mimosa Pudica", "Putri Malu atau Mimosa Pudica adalah salah satu tumbuhan yang dikategorikan kedalam jenis tumbuhan gulma dengan digolongkan kedalam anggota suku polong-polongan. Tumbuhan yang mempunyai nama latin Mimosa pudica ini tergolong kedalam keluarga Fabaceae yang berati suku polong polongan. Putri malu mudah sekali dikenali karena tumbuhan ini mempunyai sifat khas yakni, jika kita sentuh daun dari tumbuhan ini akan menciut atau layu dengan cepat, namun jika dibiarkan dalam beberapa menit daunnya akan kembali sedia kala.", "putri_malu");
        addWeed(db, "Rumput_Belulang", "Eleusine Indica", "Eleusine Indica atau yang biasa di sebut Rumput Belulang merupakan salah satu gulma tanaman budidaya yang mempunyai daya saing yang tinggi dari keluarga Poaceae atau termasuk dalam keluarga rumput-rumputan. rumput belulang berkembang biak dengan menggunakan biji (Seeds). Tanaman ini diangggap sebagai gulma karena memang sulit untuk ditangani. salah satu pengendalian rumput belulang yang ampuh yaitu dengan menggunakan herbisida.", "rumput_belulang");
        addWeed(db, "Tidak_Ada_Gulma", "-", "Gulma tidak terdeteksi", "tidak_ada_gulma_vector");

        // Add initial herbicide recommendations
        addHerbicideRecommendation(db, 1, "Tanaman Padi", "BASAGRAN 460 SL", "Herbisida sistemik dan kontak selektif purna tumbuh untuk mengendalikan gulma Alternanthera Philoxeroides, Ludiwigia Octovalvis, Spenochlea Zeylanica, Echinochloa crus-gall, Leptochloa Chinensis, teki Cyperus Iria, Fimbristylis Miliacea", "bentazon : 400 g/l");
        addHerbicideRecommendation(db, 1, "Tanaman Jagung", "CALARIS 550 SC", "Herbisida sistemik pra tumbuh dan purnah tumbuh mengendalikan gulma Ageratum Conyzoides, Alternanthera Philoxeroides, Euphorbia Prunifolia, Synedrella Nodiflora, Digitaria Ciliaris, Eleusine Indica, teki Cyperus Kyllingia", "mesotrion : 50 g/l, atrazin : 500 g/l ");
        addHerbicideRecommendation(db, 1, "Tanaman Tebu", "GROAMIN 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Phyllanthus Niruri, Cleome Asvera, Alternanthera Philoxeroides, Synedrella Nodiflora, Echinochloa Colona, Cyperus Rotundus", "2,4-D dimetil amina : 865 g/l");
        addHerbicideRecommendation(db, 1, "Tanaman Bawang Merah", "GOLTOP 240 EC", "Herbisida kontak purna tumbuh mengendalikan gulma Alternanthera Philoxeroides, galinsoga parviflora, Cynodon Dactylon, Panicum Repens,  Cyperus Rotundus", "oksifluorfen : 240 g/l");

        addHerbicideRecommendation(db, 2, "Tanaman Padi", "BENFURON 12/18 WP", "Herbisida sistemik selektif pra tumbuh dan purna tumbuh mengendalikan gulma Ludwigia Octovalvis, Monochoria Vaginalis, Limnocharis Flava, Sphenochloa Zeylanica, teki Fimbristylis Miliiacea", "metil bensulfuron : 12 %");
        addHerbicideRecommendation(db, 2, "Tanaman Jagung", "TOUCHDOWN NEO 480 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Ageratum Conyzoides, Ludwigia Octovalvis, Alternanthera Philoxeroides, Cleome Rutidosperma, Eleuthe-ranthera Ruderalis","480 g/L IPA Glifosat");
        addHerbicideRecommendation(db, 2, "Tanaman Tebu", "BASTA 150 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma, Borreria alata, Cleome Rutidosperma, Richardia Brasiliensis,  Digitaria Ciliaris", "amonium glufosinat : 150 g/l");
        addHerbicideRecommendation(db, 2, "Tanaman Bawang Merah", "GRAMOXONE 276 SL", "Herbisida kontak purna tumbuh mengendalikan gulma Ageratum Cony-zoides, Cleome Asvera, Borreria Latifolia, Synedrella Nodiflora, teki Cyperus Rotundus", "parakuat diklorida : 276 g/l");

        addHerbicideRecommendation(db, 3, "Tanaman Padi", "STARMIN 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Commelina Diffusa, Rotala Leptopela, Cyperus Iria", "2,4-D dimetil amina : 865 g/l");
        addHerbicideRecommendation(db, 3, "Tanaman Jagung", "GRAMOXONE 276 SL", "Herbisida kontak purna tumbuh mengendalikan gulma Ageratum Cony-zoides, Cleome Asvera, Borreria Latifolia, Synedrella Nodiflora, teki Cyperus Rotundus", "parakuat diklorida : 276 g/l");
        addHerbicideRecommendation(db, 3, "Tanaman Tebu", "ALLOUT 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Commelina Diffusa, Ipomea Triloba, Alternanthera Sessilis", "2,4-D dimetil amina : 865 g/l");
        addHerbicideRecommendation(db, 3, "Tanaman Bawang Merah", "GRAMOXONE 276 SL", "Herbisida kontak purna tumbuh mengendalikan gulma Ageratum Cony-zoides, Cleome Asvera, Borreria Latifolia, Synedrella Nodiflora, teki Cyperus Rotundus", "parakuat diklorida : 276 g/l");

        addHerbicideRecommendation(db, 4, "Tanaman Padi", "NOMINEE 103 OF", "Herbisida selektif dan sistemik purna tumbuh mengendalikan gulma Ipomoea aquatica, Paspalum Distichum, Cyperus Iria, Echinochloa Crusgalli", "bispiribak sodium : 103 g/l");
        addHerbicideRecommendation(db, 4, "Tanaman Jagung", "CALLISTO", "Herbisida selektif pra tumbuh dan purnah tumbuh mengendalikan gulma Ipomoea aquatica, Cleome Asvera, Borreria Latifolia, Synedrella Nodiflora", "mesotrione");
        addHerbicideRecommendation(db, 4, "Tanaman Tebu", "BASTA 150 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Borreria alata, Cleome Rutidosperma, Richardia Brasiliensis,  Digitaria Ciliaris", "amonium glufosinat : 150 g/l");
        addHerbicideRecommendation(db, 4, "Tanaman Bawang Merah", "GRAMOXONE 276 SL", "Herbisida kontak purna tumbuh mengendalikan gulma Ageratum Cony-zoides, Cleome Asvera, Borreria Latifolia, Synedrella Nodiflora, teki Cyperus Rotundus", "parakuat diklorida : 276 g/l");

        addHerbicideRecommendation(db, 5, "Tanaman Padi", "NOMINEE 103 OF", "Herbisida selektif dan sistemik purna tumbuh mengendalikan gulma Ipomoea aquatica, Paspalum Distichum, Cyperus Iria, Echinochloa Crusgalli", "bispiribak sodium : 103 g/l");
        addHerbicideRecommendation(db, 5, "Tanaman Jagung", "AMEXONE 500 SC", "Herbisida selektif sistemik pra tumbuh mengendalikan gulma Euphorbia Heterophylla, Eleusine Indica, Digitaria sp., Echinochloa Colonum, Cyperus Rotundus", "ametrin : 500 g/l");
        addHerbicideRecommendation(db, 5, "Tanaman Tebu", "BIMARON 80 WP", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Euphorbia Heterophylla", "diuron : 80,36 %");
        addHerbicideRecommendation(db, 5, "Tanaman Bawang Merah", "STOMPXTRA 450 CS", "Herbisida selektif pra tumbuh mengendalikan gulma Cleome Rutidosperma, Ipomoea Triloba, Richardia Brasiliensis, Spigelia Anthelmia, Mimosa Invisa", "pendimetalin : 450 g/l");

        addHerbicideRecommendation(db, 6, "Tanaman Padi", "ROUNDUP", "Herbisida sistemik purna tumbuh mengendalikan gulma Imperata Cylindrica", "isopropil amina glifosat : 660 g/l");
        addHerbicideRecommendation(db, 6, "Tanaman Jagung", "", "", "");
        addHerbicideRecommendation(db, 6, "Tanaman Tebu", "", "", "");
        addHerbicideRecommendation(db, 6, "Tanaman Bawang Merah", "", "", "");

        addHerbicideRecommendation(db, 7, "Tanaman Padi", "ALADIN 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Althernanthera Sessilis, limnocharis flava, Ageratum Conyzoides, Acalypha Indica, Ludwigia Octovalvis, Portulaca Oleracea", "2,4-D dimetil amina : 865 g/l");
        addHerbicideRecommendation(db, 7, "Tanaman Jagung", "GRAMOXONE 276 SL", "Herbisida kontak purna tumbuh mengendalikan gulma Ageratum Cony-zoides, Cleome Asvera, Borreria Latifolia, Synedrella Nodiflora, teki Cyperus Rotundus", "parakuat diklorida : 276 g/l");
        addHerbicideRecommendation(db, 7, "Tanaman Tebu", "ALADIN 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Althernanthera Sessilis, limnocharis flava, Ageratum Conyzoides, Acalypha Indica, Ludwigia Octovalvis, Portulaca Oleracea", "2,4-D dimetil amina : 865 g/l");
        addHerbicideRecommendation(db, 7, "Tanaman Bawang Merah", "GOLTOP 240 EC", "Herbisida kontak purna tumbuh mengendalikan gulma Alternanthera Philoxeroides, Galinsoga Parviflora, Cynodon Dactylon, Panicum Repens,  Cyperus Rotundus", "oksifluorfen : 240 g/l");

        addHerbicideRecommendation(db, 8, "Tanaman Padi", "AMEXONE 500 SC", "Herbisida selektif sistemik pra tumbuh mengendalikan gulma Cleome Rutidosperma, Richardia Brasiliensis, Echinochloa Colona, Digitaria", "ametrin : 500 g/l");
        addHerbicideRecommendation(db, 8, "Tanaman Jagung", "AKALIS 550 SC", "Herbisida sistemik selektif pra tumbuh dan purna tumbuh mengendalikan gulma, Synedrella Nodiflora, Cleome Rutidosperma, Ageratum Conyzoides, Eleusine Indica", "atrazin : 500 g/l");
        addHerbicideRecommendation(db, 8, "Tanaman Tebu", "ALMARIN 500 SC", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Synedrella Nodiflora, Cleome Rutidosperma, Cyperus Rotundus", "ametrin : 500 g/l");
        addHerbicideRecommendation(db, 8, "Tanaman Bawang Merah", "BELLMAC 240 EC", "Herbisida kontak selektif pra tumbuh dan purna tumbuh mengendalikan gulma Cleome Rutidosperma, Portulaca Oleracea, Digitaria", "oksifluorfen : 240 g/l");

        addHerbicideRecommendation(db, 9, "Tanaman Padi", "ALCATRAS 625 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Mimosa Pudica,  Chromolaena Odorata, Cyperus Rotundus", "isopropil amina glifosat : 625 g/l");
        addHerbicideRecommendation(db, 9, "Tanaman Jagung", "NUQUAT 276 SL", "Herbisida racun kontak purna tumbuh mengendalikan gulma Mimosa Pudica, Brachiaria Paspaloides, Eleusine Indica", "parakuat diklorida : 276 g/l");
        addHerbicideRecommendation(db, 9, "Tanaman Tebu", "AKOTRIN 500 SC", "Herbisida sistemik pratumbuh dan purna tumbuh mengendalikan gulma Mimosa Pudica, Croton Hirtus, Ipomoea Triloba, Spigelia Anthelmia, Brachiaria mutica", "ametrin : 500 g/l");
        addHerbicideRecommendation(db, 9, "Tanaman Bawang Merah", "STOMPXTRA 450 CS", "Herbisida selektif pra tumbuh mengendalikan gulma", "pendimetalin : 450 g/l");

        addHerbicideRecommendation(db, 10, "Tanaman Padi", "GRAMOXONE 276 SL", "Herbisida kontak purna tumbuh mengendalikan gulma Ageratum Cony-zoides, Cleome Asvera, Borreria Latifolia, Digitaria Cyliaris, Eleusine Indica , teki Cyperus Rotundus", "parakuat diklorida : 276 g/l");
        addHerbicideRecommendation(db, 10, "Tanaman Jagung", "AKALIS 550 SC", "Herbisida sistemik selektif pra tumbuh dan purna tumbuh mengendalikan gulma synedrella nodiflora, Ageratum Conyzoides, Eleusine Indica, Cleome Rutidosperma", "atrazin : 500 g/l");
        addHerbicideRecommendation(db, 10, "Tanaman Tebu", "ALMARIN 80 WP", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Cleome Asvera, Eleusine Indica", "ametrin : 80 %");
        addHerbicideRecommendation(db, 10, "Tanaman Bawang Merah", "CALIBER 240 EC", "Herbisida selektif kontak pra tumbuh dan purna tumbuh mengendalikan gulma Portulaca Oleracea, Echinochloa Colonum, Eleusine Indica", "oksifluorfen : 240 g/l");

        //Tanaman Padi
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "ABA 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Limnocharis Flava, Monochoria Vaginalis, Lindemia, Marsilea Crenata", "2,4-D dimetil amina : 865 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "ABIMEE 100 SC", "Herbisida sistemik purna tumbuh mengendalikan gulma Ludwigia Octovalvis,  Echinochloa Crusgalli, Fimbristilys Miliacea, Cyperus Iria", "natrium bispiribak : 100 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "ABOLISI 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma alternanthera sessilis, Cyperus Iria, Fimbristilys Littolaris", "2,4-D dimetil amina : 865 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "ADDIT 40 WG", "Herbisida pra dan purna tumbuh mengendalikan gulma Ludwigia Octovalvis, Marsilea Crenata, Monochoria Vaginalis, Ludwigia Hyssopifolia, Leptochloa Chinensis, Cyperus Iria", "etil karfentrazon : 40%");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "ADMIRAL 486 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Echinochloa Colona, Leptochloa Chinensis, Paspalum Conjugatum", "sopropil amina glifosat : 486 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "ALADIN 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Althernanthera Sessilis, limnocharis flava, Ageratum Conyzoides, Acalypha Indica, Ludwigia Octovalvis, Portulaca Oleracea", "2,4-D dimetil amina : 865 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "ALCHER 100 EC", "Herbisida sistemik purna tumbuh mengendalikan gulma Echinochloa Crusgalli", "butil sihalofop : 100 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "ALLOUT 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Ludwigia Octovalvis, Limnocharis Flava", "2,4-D dimetil amina : 865 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "ALLY 10/10 WP", "Herbisida sistemik pra dan purna tumbuh mengendalikan gulma Marsilea Crenata, Monochoria Vaginalis, Ludwigia Octovalvis, Fimbristylis Miliacea", "metil metsulfuron : 10,08 %, etil klorimuron : 10,08 %");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "ALLY 20 WG", "Herbisida sistemik pra tumbuh dan purna mengendalikan gulma Monochoria Vaginalis, Cyperus, Scirpus Juncoides", "metil metsulfuron : 20 %");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "ALLY 20 WP", "Herbisida sistemik pra dan purna tumbuh mengendalikan gulma Monochoria Vaginalis, Cyperus Juncoides", "metil metsulfuron : 20 %");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "ALLYPUS 77 WP", "Herbisida sistemik pra dan purna tumbuh mengendalikan gulma Ludwigia Octovalvis, Marsilea Crenata, Monochoria Vaginalis, Leptochloa Chinensis, Echinochloa Crusgalli, Cyperus Difformis, Fimbristylis Miliacea", "2,4-D natrium : 75 %, metil metsulfuron : 0,7 %");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "ALODI 15 WG", "Herbisida sistemik mengendalikan gulma Ludwigia Octovalvis, Marsilea Crenata, Monochoria Vaginalis", "etoksisulfuron : 15 %");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "AMANDY 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Ludwigia Octovalvis, Monochoria Vaginalis, Cyperus Iria, Fimbristilys Miliacea", "2,4-D dimetil amina : 865 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "AMCOMIN 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Limnocharis Flava, Commelina Diffusa, Salvinia Molesta, Cyperus Difformis", "2,4-D dimetil amina : 865 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "AMETRONE 252 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Ludwigia Hyssopifolia, Leptochloa Chinensis, Fimbristilys Littolaris", "sopropil amina glifosat : 243 g/l, metil metsulfuron : 9 g/");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "AMEXONE 500 SC", "Herbisida selektif sistemik pra tumbuh mengendalikan gulma Cleome Rutidosperma, Richardia brasiliensis, Digitaria", "ametrin : 500 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "AMEXONE 80 WP", "Herbisida selektif sistemik pra tumbuh mengendalikan gulma Cleome Rutidosperma, Richardia brasiliensis, Echinochloa Colona, Digitaria", "ametrin : 80 %");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "AMFURON 20 WG", "Herbisida sistemik purna tumbuh mengendalikan gulma Limnocharis Flava, Monochoria Vaginalis, Marsilea Crenata", "metil metsulfuron : 20%");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "AMINAX 865 SL", "Herbisida sitemik selektif purna tumbuh mengendalikan gulma Lindernia Crustacea, Ludwigia Octovalvis, Fimbristilys Miliacea", "2,4 D dimetil amina : 865 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "AMIRON – M 20 WG", "Herbisida sistemik pra dan purna tumbuh mengendalikan gulma Ludwigia Octovalvis, Monochoria Vaginalis, Marsilea Crenata", "metil metsulfuron : 20%");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "ANDALL 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Ludwigia Octovalvis, Spenochlea zeylanica, Cyperus Iria, Fimbristilys Littolaris", "2,4-D dimetil amina : 865 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "APURI 50 WP", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma lindernia crustacea, Ludwigia Octovalvis, Monochoria Vaginalis, Fimbristilys Littolaris", "kuinklorak : 47 %");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "AURUM 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Ludwigia Octovalvis, Monochoria Vaginalis, Limnocharis Flava", "2,4-D dimetil amina : 865 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "BANDITE 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Ludwigia Octovalvis, Monochoria Vaginalis, Cyperus Iria, Marsilea Crenata", "2,4-D dimetil amina : 865 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "BASMILANG 480 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Leptochloa Chinensis, Echinochloa Colona, Cyperus Difformis, Cyperus Distans", "iso propil amina glisofat : 480 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "BATARA 135 SL", "Herbisida kontak purna tumbuh mengendalikan gulma Eleocharis Dulcis, Cyperus sp", "parakuat diklorida : 135,2 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "BENSON 10 WP", "Herbisida sistemik purna tumbuh mengendalikan gulma Ludwigia Octovalvis, Eleusine Indica, Alternanthera Philoxeroides, Commelina Diffusa", "metil bensulfuron : 10 %");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "BESAMINE 865 SL", "Herbisida sistemik pra tumbuh mengendalikan gulma Ludwigia Octovalvis, Monochoria Vaginalis, Marsilea Crenata, Fimbristilys Miliacea", "2,4-D dimetil amina : 865 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "BIGQUAT 276 SL", "Herbisida kontak purna tumbuh mengendalikan gulma Ludwigia Octovalvis, Echinochloa Crusgalli, Cyperus Iria", "parakuat diklorida : 276 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "BILLY 20 WP", "Herbisida pra tumbuh mengendalikan gulma Ludwigia Octovalvis, Monochoria Vaginalis, Echinochloa Crusgalli, Cyperus Difformis, Cyperus Iria", "etil pirazosulfuron : 20 %");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "BIMASTAR 240/120 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Eleocharis Dulcis, Fimbristilys Miliacea", "isopropil amina glifosat : 240 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "BIODAMIN 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Ludwigia Octovalvis, Monochoria Vaginalis, Limnocharis Flava, Cyperus Difformis", "2,4-D dimetil amina : 865 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "BIOFURON 20 WG", "Herbisida sistemik purna tumbuh mengendalikan gulma Ludwigia Octovalvis, Monochoria Vaginalis, Fimbristilys Miliacea", "metil metsulfuron : 20 %");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "BMA 6 865 SL", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Ludwigia Octovalvis, Marsilea Crenata, Monochoria Vaginalis", "2,4 dimetil amina : 865 g/l ");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "BM BENTA-Q 480 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Monochoria Vaginalis, Cyperus Iria", "sodium bentazon : 480 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "BO 412 UP 580 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Echinochloa Crussgalli", "isopropil amina glifosat : 580 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "BORAL 480 SC", "Herbisida sistemik pra tumbuh mengendalikan gulma Marsilea Crenata, Monochoria Vaginalis, Echinochloa Crussgalli, Leptochloa Chinensis, Cyperus Difformis, Cyperus Iria,  Fimbristylis Littoralis", "sulfentrazon : 480 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "BRAVOXONE 276 SL", "Herbisida kontak purna tumbuh mengendalikan gulma Ludwigia Octovalvis, Monochoria Vaginalis, Leptochloa chinensis", "parakuat diklorida : 276 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "BURNOUT 120/120 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Eleocharis retroflaxa", "isopropil amina glifosat : 120,2 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Padi", "CALIBER 100 SC", "Herbisida sistemik purna tumbuh mengendalikan gulma Ludwigia Octovalvis, Echinochloa Crussgalli, Cyperus Iria, Fimbristilys Miliacea", "sodium bispiribak : 100 g/l");

        //Tanaman Jagung
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "ADENGO 315 SC", "Herbisida sistemik dan kontak pra tumbuh dan purna tumbuh mengendalikan gulma Cleome Rutidosperma, Richardia Anthelmia,  Brachiaria Mutica, Rottboellia Exaltata", "isoksaflutol : 225 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "AMCOMIN 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Ageratum Conyzoides,  Borreria Alata, Commelina Diffusa, Spigelia Anthelmia, Synedrella Nodiflora, Cyperus spp", "2,4-D dimetil amina : 865 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "AMCOTOP 280 SL", "Herbisida kontak pra tumbuh dan purna tumbuh mengendalikan gulma Ageratum Conyzoides, Borreria Alata, Paspalum Conjugatum", "parakuat diklorida : 280 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "AMCO UP 486 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Synedrella Nodiflora, Ageratum Conyzoides,  Axonopus Compressus, Brachiaria spp", "sopropil amina glifosat : 486 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "AMEGRASS 500 SC", "Herbisida sistemik pra dan purna tumbuh mengendalikan gulma Ageratum Conyzoides, Synedrella Nodiflora, Borreria Alata, Cleome Rutidosperma, Paspalum Conjugatum, Digitaria Ciliaris, Cyperus spp", "ametrin : 500 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "AMEXONE 500 SC", "Herbisida selektif sistemik pra tumbuh mengendalikan gulma Euphorbia Heterophylla, Eleusine Indica, Digitaria sp., Echinochloa Colonum, Cyperus Rotundus", "ametrin : 500 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "AMEXONE 80 WP", "Herbisida selektif sistemik pra tumbuh mengendalikan gulma Eleusine Indica, Digitaria sp, Echinochloa Colonum, Cyperus Rotundus", "ametrin : 80 %");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "ASEVTOPLUS 280 SL", "Herbisida kontak pra tumbuh dan purna tumbuh mengendalikan gulma Ageratum Conyzoides, Borreria Alata, Synedrella Nodiflora, Digitaria Ciliaris", "parakuat diklorida : 280 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "ATRANEX 90 WG", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Spigelia Anthelmia, Richardia Brasiliensis, Mimosa Invisa, Croton Hirtus", "atrazin : 90%");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "ATRANEX 50 WP", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Diodia Sarmentosa, Borreria Alata, Cleome Rutidosperma, Croton Hirtus", "atrazin : 50%");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "ATRION 80 WP", "Herbisida sistemik selektif pra tumbuh dan purna tumbuh mengendalikan gulma Ageratum Conyzoides, Synedrella Nodiflora, Richardia Brasiliensis, Eleusine Indica", "atrazin : 72 %, mesotrion : 8 %");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "AVIXA 500 SC", "Herbisida sistemik purna tumbuh mengendalikan gulma Ageratum Conyzoides, Cleome Rutidosperma, Euphorbia Heterophylla, Euphorbia hirta, Synedrella Nodiflora, Echinochloa Crusgalli, Eleusine Indica, Digitaria Ciliaris", "atrazin : 500 g/");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "BANDITE 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Ageratum Conyzoides, Borreria Alata, Commelina Benghalensis, Amaranthus spinosus, Cleome Rutidosperma, Cyperus spp", "2,4-D dimetil amina : 865 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "BASTA 150 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Ageratum Conyzoides, Cynodon Dactylon, Digitaria Ciliaris, Echinochloa Colonum, Eleusine Indica", "amonium glufosinat : 150 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "BATARA 135 SL", "Herbisida kontak purna tumbuh mengendalikan gulma Ageratum Conyzoides, Mimosa Invisa, Paspalum Conjugatum", "parakuat diklorida : 135,2 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "BEMO 276 SL", "Herbisida kontak purna tumbuh mengendalikan gulma Ageratum Conyzoides, Synedrella Nodiflora, Alternanthera Sessilis, Digitaria Ciliaris, Eleusine Indica", "parakuat diklorida : 276 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "BENXONE 276 SL", "Herbisida kontak purna tumbuh mengendalikan gulma Ageratum Conyzoides, Borreria Alata, Phylanthus Niruri, Axonopus Compressus, Cyperus Rotunudus", "parakuat diklorida : 276 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "BESTXONE 276 SL", "Herbisida kontak purna tumbuh mengendalikan gulma Ageratum Conyzoides, Synedrella Nodiflora, Borreria Repens, Setaria Plicata, Digitaria Ciliaris", "parakuat diklorida : 276 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "BIMASTAR 240/120 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Alternanthera Philoxeroides, Digitaria Ciliaris", "isopropil amina glifosat : 240 g/l, 2,4-D isopropil amina : 120 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "BIOLARIS 550 SC", "Herbisida sistemik selektif pra tumbuh dan purna tumbuh mengendalikan gulma Commelina Benghalensis, Celosia Argentea, Richardia Brasiliensis, Cleome Rutidosperma", "mesotrion : 50 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "BONUM 610 SC", "Herbisida sistemik selektif pra tumbuh dan purna tumbuh mengendalikan gulma Ageratum Conyzoides, Synedrella Nodiflora, Euphorbia Heterophylla, Euphorbia hirta, Digitaria Ciliaris", "atrazin : 555 g/l, mesotrion : 55 g/l ");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "BRAVOXONE 276 SL", "Herbisida kontak purna tumbuh mengendalikan gulma Borreria Alata, Commelina Diffusa, Synedrella Nodiflora, Digitaria Ascendens", "parakuat diklorida : 276 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "BURNOUT 120/120 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Richardia Brasiliensis,  Ipomoea Triloba, Mimosa Invisa, Brachiaria Eruciformis", "isopropil amina glifosat : 120,2 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "CALIENTE 520 SE", "Herbisida sistemik selektif pra tumbuh dan purna tumbuh mengendalikan gulma Ageratum Conyzoides, Cleome Rutidosperma, Richardia Brasiliensis, Eleusine Indica", "atrazin : 260 g/l, asetoklor : 260 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "CERTO 90 WP", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Ageratum Conyzoides, Cleome Rutidosperma, Synedrella Nodiflora, Digitaria spp, Paspalum Conjugatum", "sianazin : 90%");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "CHALENGER 276 SL", "Herbisida kontak purna tumbuh mengendalikan gulma Calopogonium Mucunoides, Ipomoea Triloba, Brachiaria Mutica, Digitaria Ciliaris, Echinochloa Colonum", "parakuat diklorida : 276 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "CONVEY 330 SC", "Herbisida pra tumbuh dan purna tumbuh mengendalikan gulma Borreria Alata, Croton Hirtus, Richardia Brasiliensis, Brachiaria Mutica", "topramezon : 330,3 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "DELTAXONE 280 SL", "Herbisida kontak purna tumbuh mengendalikan gulma Synedrella Nodiflora, Borreria Alata, Paspalum Conjugatum", "parakuat diklorida : 280 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "DK ZINE 500 SC", "Herbisida sistemik selektif pra tumbuh dan purna tumbuh mengendalikan gulma Richardia Brasiliensis, Borreria Alata, Calopogonium Mucunoides, Brachiaria Mutica", "atrazin : 500 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "DK ZINE 80 WP", "Herbisida sistemik selektif pra tumbuh dan purna tumbuh mengendalikan gulma Ipomoea Triloba, Cleome Rutidosperma, Borreria Alata, Calopogonium Mucunoides, Brachiaria Mutica", "atrazi : 80 %");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "DRYUP 480 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Euphorbia Heterophylla, Euphorbia Hirta, Ageratum Conyzoides, Borreria Alata, Cleome Asvera, Axonopus Compressus", "isopropil amina glifosat : 480 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "DUET 160 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Ageratum Conyzoides, Eleutheranthera Ruderalis, Synedrella Nodiflora, Digitaria Ciliaris, Eleusine Indica", "isopropil amina glifosat : 160 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "ELANG 480 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Ageratum Conyzoides, Borreria Alata, Synedrella Nodiflora, Digitaria Ciliaris, Eleusine Indica", "isopropil amina glifosat : 480 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "ENKAZONE 276 SL", "Herbisida kontak purna tumbuh mengendalikan gulma Ageratum Conyzoides, Borreria Alata, Synedrella Nodiflora, Digitaria Ciliaris", "parakuat diklorida : 276 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "EXTRA-ONE 680 SC", "Herbisida sistemik selektif pra tumbuh dan purna tumbuh mengendalikan gulma Ageratum Conyzoides, Synedrella Nodiflora, Borreria Alata, Richardia Brasiliensis, Digitaria Ciliaris", "atrazin : 600 g/l, mesotrion : 80 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "FIZZ 575 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Ageratum Conyzoides, Borreria Alata, Cynodon Dactylon, Digitaria Ciliaris, Eleusine Indica", "2,4-D isopropil amina : 575 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "GEMAXONE 276 SL", "Herbisida kontak pra dan purna tumbuh mengendalikan gulma Ageratum Conyzoides, Synedrella Nodiflora, Alternanthera Philoxeroides, Cleome Rutidosperma, Digitaria Ciliaris", "parakuat diklorida : 276 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "GEMPUR 480 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Ageratum Conyzoides, Synedrella Nodiflora, Paspalum Conjugatum, Digitaria spp, Axonopus Compressus", "sopropil amina glifosat : 480 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "GERXONE 288 SL", "Herbisida kontak purna tumbuh mengendalikan gulma Synedrella Nodiflora, Borreria Alata, Paspalum Conjugatum, Eleusine Indica", "parakuat diklorida : 288 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "GESAPAX 500 SC", "Herbisida sistemik pra mengendalikan gulma Ageratum Conyzoides, Borreria Latifolia, Cleome Rutidosperma, Croton Hirtus, Eupatorium Odoratum, Mimosa Invisa, Mimosa Pudica,  Digitaria Sanguinalis, Eleusine Indica, Paspalum Conjugatum, ", "ametrin : 490 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Jagung", "GIBAS 240 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Ageratum Conyzoides, Eleutheranthera Ruderalis, Synedrella Nodiflora, Digitaria Ciliaris, Eleusine Indica" , "isopropil amina glifosat : 240 g/l");

        //Tanaman Tebu
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "ABOLISI 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Asystasia Gangetica, Croton Hirtus, Euphorbia Geniculata, Euphorbia Heterophylla, Ipomoea Triloba, Richardia Brasiliensis", "2,4-D dimetil amina : 865 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "AKOTRIN 500 SC", "Herbisida sistemik pratumbuh dan purna tumbuh mengendalikan gulma Croton Hirtus, Ipomoea Triloba, Mimosa Indica, Mimosa Pudica, Spigelia Anthelmia, Brachiaria Mutica", "ametrin : 500 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "ALADIN 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Ageratum Conyzoides, Acalypha Indica, Phyllanthus Nirur, Leucas Lavandulifolia, Ludwigia Peruviana, Ludwigia Octovalvis", "2,4-D dimetil amina : 865 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "AlFAMIN 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Calopogonium Mucunoides, Borreria Alata, Cleome Asvera, Mimosa Invisa", "2,4-D dimetil amina : 865 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "ALLOUT 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Ipomoea Triloba, Alternanthera Sessilis, Commelina Diffusa, Alternanthera Philoxeroides", "2,4-D dimetil amina : 865 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "ALMARIN 500 SC", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Synedrella Nodiflora, Cleome Rutidosperma, Rottboellia Exaltata, Cyperus Rotundus", "ametrin : 500 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "ALMARIN 80 WP", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Cleome Asvera, Eleusine Indica", "ametrin : 80 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "AMANDY 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Ageratum Conyzoides, Cleome Rutidosperma, Cleome Asvera, Borreria Alata, Echinochloa Colona, Cyperus Rotundus", "2,4-D dimetil amina : 865 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "AMCOMETRYN 80 WP", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Borreria Alata, Calopogonium Mucunoides, Cleome Asvera", "ametrin : 80 %");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "AMCOMIN 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Borreria Latifolia, Ageratum Conyzoides", "2,4-D dimetil amina : 865 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "AMEGRASS 500 SC", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Cleome Asvera, Phyllanthus Naruri, Cynodon Dactylon, Digitaria Sanguinalis, Eclipta Prostrata", "ametrin : 500 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "AMEGRASS 80 WP", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Mikania Cordata, Borreria sp", "ametrin : 80 %");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "AMETREX 500 SC", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Celosia Argentea, Commelina Benghalensis, Richardia Brasiliensis, Brachiaria Mutica, Spigelia Anthelmia", "ametrin : 500 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "AMETREX 80 WP", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Cleome Rutidosperma, Ipomoea Triloba, Richardia Anthelmia, Brachiaria Mutica, Spigelia Anthelmia", "ametrin : 80 %");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "AMETOX 500 SC", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Borreria Alata, Cleome Rutidosperma Richardia Anthelmia, Brachiaria Mutica", "ametrin : 500 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "AMEXONE 500 SC", "Herbisida selektif sistemik pra tumbuh mengendalikan gulma Ageratum Conyzoides, Borreria Alata, Cleome Rutidosperma, Echinochloa Colona, Cyperus Iria, Fimbristylis Littoralis", "ametrin : 500 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "AMEXONE 80 WP", "Herbisida selektif sistemik pra tumbuh mengendalikan gulma Ageratum Conyzoides, Borreria Alata, Cleome Rutidosperma, Echinochloa Colona, Cyperus Iria, Fimbristylis Littoralis", "ametrin : 80 %");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "AMTRAK 500 SC", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Borreria Latifolia, Cleome Rutidosperma, Synedrella Nodiflora, Ischaemum Rugosum", "ametrin : 500 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "AMTRAK 80 WP", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Ageratum Conyzoides, Borreria Alata, Cleome Rutidosperma, Ottochloa Nodosa, Paspalum Conjugatum, Setaria Plicata", "ametrin : 80 %");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "ANDALL 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Euphorbia Heterophylla, Eclipta Prostrata, Alternanthera Sessilis, Alternanthera Philoxeroides, Melochia Corchorifolia, Ageratum Conyzoides", "2,4-D dimetil amina : 865 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "BANDITE 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Ageratum Conyzoides, Borreria Alata, Synedrella Nodiflora, Cleome Rutidosperma, Mimosa Invisa, Mimosa Pudica", "2,4-D dimetil amina : 865 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "BARON 500 SC", "Herbisida selektif sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Borreria Alata, Cleome Asvera, Brachiaria Mutica, Digitaria Adscendens, Echinochloa Colona", "ametrin : 500 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "BARON 80 WP", "Herbisida selektif sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Borreria Alata, Cleome Asvera, Brachiaria Mutica, Digitaria Adscendens, Echinochloa Colona", "ametrin : 80 %");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "BASTA 150 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Borreria Alata, Cleome Rutidosperma, Richardia Brasiliensis, Digitaria Ciliaris", "amonium glufosinat : 150 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "BILIS 500 SC", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Ageratum Conyzoides, Borreria Latifolia, Euphorbia Hirta, Paspalum Conjugatum", "ametrin : 500 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "BIMARON 500 SC", "Herbisida sistemik selektif pra tumbuh dan purna tumbuh mengendalikan gulma Digitaria sp", "diuron : 500 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "BIMARON 80 WP", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Euphorbia sp", "diuron : 80,36 %");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "BIMASTAR 240/120 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Synedrella Nodiflora", "isopropil amina glifosat : 240 g/l, 2,4-D isopropil amina : 120 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "BIORON 80 WP", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Borreria Alata, Euphorbia Hirta, Ageratum Conyzoides, Centrosema Pubescens, Calopogonium Mucunoides, Axonopus Compressus", "diuron : 80 %");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "BORAL 480 SC", "Herbisida sistemik pra tumbuh mengendalikan gulma Croton Hirtus, Commelina Nudiflora, Commelina Diffusa, Cleome Rutidosperma", "sulfentrazon : 480 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "BRAVOXONE 276 SL", "Herbisida kontak purna tumbuh mengendalikan gulma Ageratum Conyzoides, Mimosa Invisa, Paspalum Conjugatum", "parakuat diklorida : 276 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "BURON 80 WP", "Herbisida sistemik purna tumbuh mengendalikan gulma Borreria Alata, Calopogonium Mucunoides, Cleome Asvera, Digitaria Ciliaris", "diuron : 80 %");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "CADRE 240 SL", "Herbisida selektif purna tumbuh mengendalikan gulma Cleome Sp, Digitaria Sp", "imazapik : 240 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "CBA-6 865 SL", "Herbisida sistemik pra tumnbuh dan purna tumbuh mengendalikan gulma Ageratum Conyzoides, Richardia Brasiliensis, Synedrella Nodiflora", "2,4-D dimetil amina : 865 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "CENTARON 80 WP", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Borreria Alata, Mimosa Invisa, Cleome Asvera", "diuron : 80 %");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "CENTATRIN 500 SC", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Borreria Alata, Cleome Asvera", "ametrin : 500 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "COPRAL 500 SC", "Herbisida sistemik selektif pra tumbuh dan purna tumbuh mengendalikan gulma Ageratum Conyzoides, Euphorbia Hirta, Synedrella Nodiflora, Echinochloa Colona", "ametrin : 500 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "DACOMIN 865 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Ageratum Conyzoides, Borreria Alata, Diodia Sarmentosa", "2,4-D dimetil amina : 865 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "DAIMEX 80 WP", "Herbisida sistemik selektif pra tumbuh dan purna tumbuh mengendalikan gulma Cleome Rutidosperma, Euphorbia Hirta, Synedrella Nodiflora, Echinochloa Colona, Euphorbia Heterophylla", "diuron : 80 %");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "DIMINA 720 SL", "Herbisida sistemik purna tumbuh mengendalikan gulma Ageratum Conyzoides, Borreria Alata, Ipomoea Triloba", "2,4-D dimetil amina : 720 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Tebu", "DIRONEX 80 WP", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Ageratum Conyzoides, Borreria Alata, Cleome Rutidosperma, Mimosa Invisa, Axonopus Compressus, Digitaria Adscendens", "diuron : 80 %");

        //Tanaman Bawang Merah
        addAdditionalHerbicideRecommendation(db, "Tanaman Bawang Merah", "ABOS 240 EC", "Herbisida kontak pra tumbuh dan purna tumbuh mengendalikan gulma Digitaria Ciliaris, Cyperus sp", "oksifluorfen : 240 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Bawang Merah", "ADJUST 240 EC", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Portulaca Oleracea, Paspalum Conjugatum, Cyperus spp", "oksifluorfen : 240 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Bawang Merah", "BULDOZER 240 EC", "Herbisida kontak mengendalikan gulma Portulaca Oleracea, Alternanthera Sessilis, Alternanthera Philoxeroides, Echinochloa Colona, Fimbristylis Miliacea, Cyperus sp", "2,4-D dimetil amina : 865 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Bawang Merah", "DOST 330 EC", "Herbisida sistemik purna tumbuh mengendalikan gulma Alternanthera Sessilis, Alternanthera Philoxeroides, Ageratum Conyzoides, Cynodon Dactylon, Echinochloa Colona", "pendimetalin : 330 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Bawang Merah", "ECLIPSE 240 SC", "Herbisida kontak pra tumbuh dan purna tumbuh mengendalikan gulma Portulaca Oleracea, Amaranthus Spinosus, Echinochloa Colona, Cyperus Iria", "oksifluorfen : 240 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Bawang Merah", "GOAL 240 EC", "Herbisida kontak pra tumbuh mengendalikan gulma Ageratum Conyzoides, Amaranthus Spinosus, Borreria Alata, Mimosa Invisa, Mimosa Pudica, Clibadium Surinamense, Synedrella Nodiflora, Echinochloa Colona, Eleusine Indica, Portulaca Oleracea, Paspalum sp", "oksifluorfen : 240 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Bawang Merah", "GOLMA 240 EC", "Herbisida kontak pra dan purna tumbuh mengendalikan gulma Portulaca Oleracea, Echinochloa Colona, Eleusine Indica", "oksifluorfen : 240 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Bawang Merah", "GOLOK 240 EC", "Herbisida kontak pra tumbu mengendalikan gulma Leptochloa Chinensis, Cyperus Difformis", "oksifluorfen : 240 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Bawang Merah", "GULL 240 EC", "Herbisida kontak pra dan purna tumbuh mengendalikan gulma Amaranthus Lividus, Portulaca Oleracea, Echinochloa Colona", "oksifluorfen : 240 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Bawang Merah", "NUGRASS 69 EC", "Herbisida sistemik purna tumbuh mengendalikan gulma Echinochloa Crusgalli, Eleusine Indica", "fenoksaprop-p-etil : 69 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Bawang Merah", "PRODUCE 240 EC", "Herbisida kontak mengendalikan gulma Ageratum Conyzoides, Portulaca Oleracea, Synedrella Nodiflora, Digitaris Ciliaris", "oksifluorfen : 240 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Bawang Merah", "PROWL 330 EC", "Herbisida pra tumbuh mengendalikan gulma Ageratum Conyzoides, Alternanthera Philoxeroides, Amaranthus Spinosus, Erechtites Valerianifolius, Cynodon Dactylon, Digitaris sp, Echinochloa Colona, Eleusine Indica", "pendimetalin : 330 g/");
        addAdditionalHerbicideRecommendation(db, "Tanaman Bawang Merah", "RUDSTAR 250 EC", "Herbisida sistemik pra tumbuh dan purna tumbuh mengendalikan gulma Ageratum Conyzoides, Alternanthera Sessilis, Echinochloa Colona, Cyperus Difformis, Fimbristylis Littoralis", "oksadiazon : 250 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Bawang Merah", "RUMPAS 120 EW", "Herbisida sistemik dan kontak purna tumbuh mengendalikan gulma Alternanthera Sessilis, Amaranthus Sp, Digitaria Adscendens, Cyperus Iria", "fenoksaprop-p-etil : 120 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Bawang Merah", "SELECT 240 EC", "Herbisida sistemik purna tumbuh mengendalikan gulma Amaranthus Spinosus, Portulaca Oleracea, Echinochloa Colona", "kletodim : 240 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Bawang Merah", "VALANI 330 EC", "Herbisida pra tumbuh mengendalikan gulma Alternanthera Sessilis, Cleome asvera, cleome rutidosperma, commelina diffusa, axonopus compressus, Echinochloa Colona", "pendimetalin : 330 g/l");
        addAdditionalHerbicideRecommendation(db, "Tanaman Bawang Merah", "ZERAM 250 EC", "Herbisida kontak pra dan purna tumbuh mengendalikan gulma Portulaca Oleracea, Digitaris sp, Cyperus sp", "oksifluorfen : 250 g/l");
    }

    private void addWeed(SQLiteDatabase db, String weedName, String scientificName, String description, String imageUrl) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_WEED_NAME, weedName);
        values.put(COLUMN_SCIENTIFIC_NAME, scientificName);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_IMAGE_URL, imageUrl);
        db.insert(TABLE_WEEDS, null, values);
    }

    private void addHerbicideRecommendation(SQLiteDatabase db, int weedId, String cropType, String herbicideName, String description, String activeIngredient) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_WEED_ID, weedId);
        values.put(COLUMN_CROP_TYPE, cropType);
        values.put(COLUMN_HERBICIDE_NAME, herbicideName);
        values.put(COLUMN_DESCRIPTION_HERBICIDE, description);
        values.put("active_ingredient", activeIngredient); // Tambahkan kolom active_ingredient
        db.insert(TABLE_HERBICIDE_RECOMMENDATIONS, null, values);
    }

    private void addAdditionalHerbicideRecommendation(SQLiteDatabase db, String cropType, String herbicideName, String description, String activeIngredient) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ADDITIONAL_CROP_TYPE, cropType);
        values.put(COLUMN_ADDITIONAL_HERBICIDE_NAME, herbicideName);
        values.put(COLUMN_ADDITIONAL_DESCRIPTION, description);
        values.put("active_ingredient", activeIngredient); // Tambahkan kolom active_ingredient
        db.insert(TABLE_ADDITIONAL_HERBICIDE_RECOMMENDATIONS, null, values);
    }

    public Cursor getAdditionalHerbicideRecommendationsByCropType(String cropType) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ADDITIONAL_HERBICIDE_RECOMMENDATIONS + " WHERE " + COLUMN_ADDITIONAL_CROP_TYPE + "=?", new String[]{cropType});
    }

    public Cursor getWeedDetails(String weedName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " +
                TABLE_WEEDS + ".*, " +
                "GROUP_CONCAT(" + TABLE_HERBICIDE_RECOMMENDATIONS + "." + COLUMN_CROP_TYPE + " || ': ' || " + TABLE_HERBICIDE_RECOMMENDATIONS + "." + COLUMN_HERBICIDE_NAME + ", ', ') AS " + COLUMN_HERBICIDE_NAME + " " +
                "FROM " + TABLE_WEEDS +
                " LEFT JOIN " + TABLE_HERBICIDE_RECOMMENDATIONS +
                " ON " + TABLE_WEEDS + "." + COLUMN_ID + " = " + TABLE_HERBICIDE_RECOMMENDATIONS + "." + COLUMN_WEED_ID +
                " WHERE " + COLUMN_WEED_NAME + "=? GROUP BY " + TABLE_WEEDS + "." + COLUMN_ID;
        return db.rawQuery(query, new String[]{weedName});
    }

    public Cursor getHerbicideRecommendations(int weedId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_HERBICIDE_RECOMMENDATIONS + " WHERE " + COLUMN_WEED_ID + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(weedId)});
        if (cursor != null && cursor.getCount() > 0) {
            Log.d("DatabaseGulma", "Found herbicide recommendations for weed ID: " + weedId);
        } else {
            Log.d("DatabaseGulma", "No herbicide recommendations found for weed ID: " + weedId);
        }
        return cursor;
    }


    public void addWeedIdentification(String identifiedWeed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IDENTIFIED_WEED, identifiedWeed);
        long result = db.insert(TABLE_IDENTIFICATIONS, null, values);
        if (result == -1) {
            Log.d("DatabaseGulma", "Failed to add weed identification: " + identifiedWeed);
        } else {
            Log.d("DatabaseGulma", "Successfully added weed identification: " + identifiedWeed);
        }
        db.close();
    }

    public Cursor getWeedIdentificationHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT identifications." + COLUMN_IDENTIFICATION_ID + ", " +
                "identifications." + COLUMN_IDENTIFIED_WEED + ", " +
                "weeds." + COLUMN_SCIENTIFIC_NAME + ", " +
                "weeds." + COLUMN_IMAGE_URL + ", " +
                "identifications." + COLUMN_IDENTIFICATION_TIME +
                " FROM " + TABLE_IDENTIFICATIONS + " AS identifications " +
                "JOIN " + TABLE_WEEDS + " AS weeds " +
                "ON identifications." + COLUMN_IDENTIFIED_WEED + " = weeds." + COLUMN_WEED_NAME +
                " ORDER BY identifications." + COLUMN_IDENTIFICATION_TIME + " DESC";
        return db.rawQuery(query, null);
    }

    public Cursor getHerbicideRecommendationsByCropType(String cropType) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_HERBICIDE_RECOMMENDATIONS + " WHERE " + COLUMN_CROP_TYPE + "=?", new String[]{cropType});
    }

    public void deleteWeedIdentification(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_IDENTIFICATIONS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
