package com.example.muvitracker.ui.main.providers


enum class AppCountry(
    val code: String,
    val displayName: String,
) {

    ARGENTINA("ar", "Argentina"),
    AUSTRALIA("au", "Australia"),
    AUSTRIA("at", "Austria"),
    BELGIUM("be", "Belgium"),
    BRAZIL("br", "Brazil"),
    BULGARIA("bg", "Bulgaria"),
    CANADA("ca", "Canada"),
    CHILE("cl", "Chile"),
    COLOMBIA("co", "Colombia"),
    CZECH_REP("cz", "Czech Republic"),
    DENMARK("dk", "Denmark"),
    ECUADOR("ec", "Ecuador"),
    ESTONIA("ee", "Estonia"),
    FINLAND("fi", "Finland"),
    FRANCE("fr", "France"),
    GERMANY("de", "Germany"),
    GREECE("gr", "Greece"),
    HONGKONG("hk", "Hong Kong"),
    HUNGARY("hu", "Hungary"),
    INDIA("in", "India"),
    INDONESIA("id", "Indonesia"),
    IRELAND("ie", "Ireland"),
    ITALY("it", "Italy"),
    JAPAN("jp", "Japan"),
    LATVIA("lv", "Latvia"),
    LITHUANIA("lt", "Lithuania"),
    MALAYSIA("my", "Malaysia"),
    MEXICO("mx", "Mexico"),
    MOLDOVA("md", "R. Moldova"),
    NETHERLANDS("nl", "Netherlands"),
    NEW_ZEALAND("nz", "New Zealand"),
    NORWAY("no", "Norway"),
    PERU("pe", "Peru"),
    PHILIPPINES("ph", "Philippines"),
    POLAND("pl", "Poland"),
    PORTUGAL("pt", "Portugal"),
    ROMANIA("ro", "Romania"),
    RUSSIA("ru", "Russia"),
    SINGAPORE("sg", "Singapore"),
    SOUTH_AFRICA("za", "South Africa"),
    SOUTH_KOREA("kr", "South Korea"),
    SPAIN("es", "Spain"),
    SWEDEN("se", "Sweden"),
    SWITZERLAND("ch", "Switzerland"),
    THAILAND("th", "Thailand"),
    TAIWAN("tw", "Taiwan"),
    TURKEY("tr", "Turkey"),
    UKRAINE("ua", "Ukraine"),
    UNITED_KINGDOM("uk", "United Kingdom"),
    UNITED_STATES("us", "United States"),
    VENEZUELA("ve", "Venezuela");

    // moldova ok
    // add portughese, french TODO


    // altre europa TODO - da lista tmdb
    companion object {
        // values == lista istanza di AppCountry
        fun fromCode(code: String) = AppCountry.entries.first { it.code == code }
    }
}


/* paesi disponibili in tmdb json

"AD","AE","AG","AL","AO","AR","AT","AU","AZ",
"BA","BB","BE","BG","BH","BM","BO","BR","BS","BY","BZ",
"CA","CH","CI","CL","CM","CO","CR","CU","CV","CY","CZ",
"DE","DK","DO","DZ",
"EC","EE","EG","ES",
"FI","FJ","FR",
"GB","GF","GG","GH","GI","GQ","GR","GT",
"HK","HN","HR","HU",
"ID","IE","IL","IN","IQ","IS","IT",
"JM","JO","JP",
"KE","KR","KW",
"LB","LC","LI","LT","LU","LV","LY",
"MA","MC","MD","ME","MG","MK","ML","MT","MU","MX","MY","MZ",
"NE","NG","NI","NL","NO","NZ",
"OM",
"PA","PE","PF","PH","PK","PL","PS","PT","PY",
"QA",
"RO","RS",
"SA","SC","SE","SG",
"SH","SI","SJ","SK","SL","SM","SN","SO","SR","SS","ST","SV","SX","SY","SZ",
"TC","TD","TF","TG","TH","TJ","TK","TL","TM","TN","TO","TR","TT","TV","TW","TZ",
"UA","UG","UM","US","UY","UZ",
"VA","VC","VE","VG","VI","VN","VU",
"WF","WS",
"YE","YT",
"ZA","ZM","ZW"
 */
