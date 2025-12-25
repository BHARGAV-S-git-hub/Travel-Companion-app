package travelguide;

import java.util.*;

public class CityData {
    private static final Map<String, CityInfo> cities = new HashMap<>();
    private static final Map<String, List<String>> topCitiesByState = new HashMap<>();

    static {
        cities.put("Karnataka", new CityInfo(
            "Karnataka", "India", "South India",
            12.9716, 77.5946,
            "Karnataka is a diverse state known for its technology hub Bengaluru, rich heritage sites like Hampi, and lush Western Ghats.",
            "Tropical savanna / monsoon", "October-February",
            78, 80, 35,
            "Karnataka has a long history from the Vijayanagara Empire to modern technology-driven growth centered in Bengaluru.",
            Arrays.asList("Bangalore Palace", "Hampi (Warangal ruins)", "Mysore Palace", "Coorg", "Nagarhole National Park"),
            Arrays.asList("The Leela Palace Bengaluru", "Taj West End", "ITC Windsor", "Eagleton Resort"),
            Arrays.asList("Karavalli", "MTR", "Vidyarthi Bhavan", "Koshy's")
        ));

        cities.put("Tamil Nadu", new CityInfo(
            "Tamil Nadu", "India", "South India",
            13.0827, 80.2707,
            "Tamil Nadu features ancient Dravidian temples, classical arts, and a long coastline anchored by Chennai.",
            "Tropical wet and dry", "November-February",
            72, 75, 40,
            "With dynasties such as the Cholas and Pandyas, Tamil Nadu has a recorded history spanning millennia.",
            Arrays.asList("Meenakshi Temple (Madurai)", "Brihadeeswarar Temple (Thanjavur)", "Marina Beach", "Mahabalipuram"),
            Arrays.asList("Taj Coromandel", "ITC Grand Chola", "The Leela Palace Chennai", "Radisson Blu Temple Bay"),
            Arrays.asList("Murugan Idli Shop", "Saravana Bhavan", "Dakshin", "Anjappar")
        ));

        cities.put("Maharashtra", new CityInfo(
            "Maharashtra", "India", "West India",
            19.0760, 72.8777,
            "Maharashtra is home to Mumbai — India's financial capital — as well as the Ajanta and Ellora caves and diverse coastal towns.",
            "Tropical monsoon", "October-March",
            70, 78, 48,
            "From ancient Buddhist cave complexes to colonial Mumbai, Maharashtra blends history and modern commerce.",
            Arrays.asList("Gateway of India", "Ajanta & Ellora Caves", "Shirdi", "Sanjay Gandhi National Park"),
            Arrays.asList("The Taj Mahal Palace", "The Oberoi Mumbai", "ITC Maratha", "Four Seasons Mumbai"),
            Arrays.asList("The Table", "Leopold Cafe", "Bademiya", "Britannia & Co.")
        ));

        cities.put("Delhi", new CityInfo(
            "Delhi", "India", "North India",
            28.7041, 77.1025,
            "Delhi, the national capital region, combines Mughal-era monuments, bustling markets and modern neighborhoods.",
            "Humid subtropical", "October-March",
            65, 70, 55,
            "Delhi's history spans several empires — from the Delhi Sultanate and Mughals to the British Raj and modern India.",
            Arrays.asList("Red Fort", "Qutub Minar", "India Gate", "Humayun's Tomb", "Connaught Place"),
            Arrays.asList("The Leela Palace New Delhi", "Taj Mahal Hotel", "ITC Maurya", "Oberoi New Delhi"),
            Arrays.asList("Karim's", "Bukhara", "Indian Accent", "Diggin")
        ));

        cities.put("Kerala", new CityInfo(
            "Kerala", "India", "South India",
            10.8505, 76.2711,
            "Kerala is known for its backwaters, lush hill stations, spice gardens and rich maritime history.",
            "Tropical monsoon / rainforest", "September-March",
            84, 82, 30,
            "Kerala's maritime trade links and unique matrilineal social systems make its cultural history distinctive in India.",
            Arrays.asList("Alleppey Backwaters", "Munnar Tea Plantations", "Kovalam Beach", "Periyar Wildlife Sanctuary"),
            Arrays.asList("Kumarakom Lake Resort", "Taj Malabar, Kochi", "Carnoustie Ayurveda & Wellness Resort", "Leela Kovalam"),
            Arrays.asList("The Rice Boat", "Paragon", "Fort House (Kovalam)", "Kayees")
        ));

        cities.put("West Bengal", new CityInfo(
            "West Bengal", "India", "East India",
            22.9868, 87.8550,
            "West Bengal's capital Kolkata is a cultural hub known for literature, festivals, colonial architecture and Bengali cuisine.",
            "Tropical wet and dry", "October-March",
            68, 70, 50,
            "Bengal has a long cultural and political history, from the Bengal Sultanate to British-era Calcutta and modern India.",
            Arrays.asList("Victoria Memorial", "Sundarbans National Park", "Howrah Bridge", "Dakshineswar Kali Temple"),
            Arrays.asList("The Oberoi Grand", "ITC Sonar", "Taj Bengal", "The Park Kolkata"),
            Arrays.asList("Oh! Calcutta", "6 Ballygunge Place", "Peter Cat", "Bhojohori Manna")
        ));

        cities.put("Rajasthan", new CityInfo(
            "Rajasthan", "India", "Northwest India",
            27.0238, 74.2179,
            "Rajasthan is famous for its desert forts, palaces, folk music and vibrant festivals centered around Jaipur, Jodhpur and Udaipur.",
            "Arid / semi-arid", "October-March",
            60, 66, 58,
            "The Rajput kingdoms and Mughal interactions shaped Rajasthan's martial and architectural traditions over centuries.",
            Arrays.asList("Amber Fort", "City Palace (Udaipur)", "Jaisalmer Fort", "Mehrangarh Fort"),
            Arrays.asList("Rambagh Palace", "City Palace Hotel Udaipur", "Taj Hari Mahal", "Rajvilas"),
            Arrays.asList("Chokhi Dhani (Jaipur dining)", "Suvarna Mahal", "Handi (jaipur)", "LMB (Jaipur)")
        ));

        cities.put("Uttar Pradesh", new CityInfo(
            "Uttar Pradesh", "India", "North India",
            26.8467, 80.9462,
            "Uttar Pradesh is home to Varanasi, the Taj Mahal in Agra, and many of India's most important religious and historic sites.",
            "Humid subtropical", "October-March",
            55, 60, 66,
            "As the cradle of empires and religions, UP has been central to Indian history from ancient to modern times.",
            Arrays.asList("Taj Mahal (Agra)", "Varanasi Ghats", "Sarnath", "Fatehpur Sikri"),
            Arrays.asList("Tajview Agra", "Ramada Plaza Agra", "Vivanta by Taj (Varanasi)", "ITC Mughal"),
            Arrays.asList("Tunday Kababi", "Pind Balluchi", "Deena Nath Ji", "Baba Nagpal")
        ));

        cities.put("Telangana", new CityInfo(
            "Telangana", "India", "South-Central India",
            17.3850, 78.4867,
            "Telangana features Hyderabad with its historic Charminar, rich cuisine and growing tech industry.",
            "Semi-arid / tropical", "October-February",
            71, 76, 42,
            "Hyderabad's history under the Nizams produced iconic monuments, pearls trade and a distinctive Hyderabadi culture.",
            Arrays.asList("Charminar", "Golconda Fort", "Ramoji Film City", "Hussain Sagar Lake"),
            Arrays.asList("Taj Falaknuma Palace", "ITC Kakatiya", "Novotel Hyderabad", "Trident Hyderabad"),
            Arrays.asList("Paradise Biryani", "Bawarchi", "Ohri's", "Chutneys")
        ));

        cities.put("Gujarat", new CityInfo(
            "Gujarat", "India", "West India",
            23.0225, 72.5714,
            "Gujarat is known for its rich trading history, Gir wildlife, Rann of Kutch, and vibrant textile and handicraft traditions.",
            "Semi-arid", "November-February",
            67, 68, 45,
            "From ancient Indus Valley links to maritime trade and the modern industrial economy, Gujarat has a long commercial history.",
            Arrays.asList("Sabarmati Ashram", "Rann of Kutch", "Gir National Park", "Somnath Temple"),
            Arrays.asList("Taj Mahal Palace (Bhavnagar)", "Ginger Hotels", "The Fern Residency", "Hyatt Regency Ahmedabad"),
            Arrays.asList("Gordhan Thal", "Agashiye", "Manek Chowk street food", "The Green House")
        ));

        // --- per-city entries (top cities for states) ---
        cities.put("Bengaluru", new CityInfo("Bengaluru", "India", "Karnataka", 12.9716, 77.5946,
            "Bengaluru (Bangalore) — capital and major tech hub.", "Tropical savanna / monsoon", "Oct-Feb",
            80, 82, 30, "Modern technology and garden city.",
            Arrays.asList("Bangalore Palace","Lalbagh Botanical Garden"),
            Arrays.asList("Taj West End","ITC Windsor"),
            Arrays.asList("MTR","Vidyarthi Bhavan")
        ));
        cities.put("Mysore", new CityInfo("Mysore", "India", "Karnataka", 12.2958, 76.6394,
            "Historic city famed for Mysore Palace.", "Tropical", "Oct-Mar",
            82, 78, 28, "Rich royal history and culture.",
            Arrays.asList("Mysore Palace","Chamundi Hill"),
            Arrays.asList("Royal Orchid Metropole","The Windflower"),
            Arrays.asList("Darshini","RRR Meals")
        ));
        cities.put("Mangalore", new CityInfo("Mangalore", "India", "Karnataka", 12.9141, 74.8560,
            "Coastal city with beaches and port.", "Tropical monsoon", "Nov-Feb",
            76, 74, 34, "Coastal cuisine and ports.",
            Arrays.asList("Panambur Beach","St. Aloysius Chapel"),
            Arrays.asList("Goldfinch","The Ocean Pearl"),
            Arrays.asList("Giri Manja's","Ideal Cafe")
        ));
        cities.put("Hubli", new CityInfo("Hubli", "India", "Karnataka", 15.3647, 75.1238,
            "Commercial hub in northern Karnataka.", "Tropical", "Oct-Mar",
            74, 70, 38, "Important trade center.",
            Arrays.asList("Chandramouleshwara Temple"),
            Arrays.asList("Hotel Rock Regency"),
            Arrays.asList("Local eateries")
        ));
        cities.put("Belgaum", new CityInfo("Belgaum", "India", "Karnataka", 15.8497, 74.4977,
            "Border city with historical forts.", "Tropical", "Oct-Mar",
            75, 71, 36, "Historical forts and hill regions.",
            Arrays.asList("Belgaum Fort"),
            Arrays.asList("Hotel Kamat"),
            Arrays.asList("Local cuisine")
        ));

        // Tamil Nadu top cities
        cities.put("Chennai", new CityInfo("Chennai", "India", "Tamil Nadu", 13.0827, 80.2707,
            "Capital city with cultural and economic importance.", "Tropical wet and dry", "Nov-Feb",
            70, 74, 45, "Coastal metropolis with temples and beaches.",
            Arrays.asList("Marina Beach","Kapaleeshwarar Temple"),
            Arrays.asList("Taj Coromandel","ITC Grand Chola"),
            Arrays.asList("Murugan Idli Shop","Saravana Bhavan")
        ));
        cities.put("Madurai", new CityInfo("Madurai", "India", "Tamil Nadu", 9.9252, 78.1198,
            "Ancient temple city.", "Tropical", "Oct-Mar",
            78, 70, 32, "Famous Meenakshi temple.",
            Arrays.asList("Meenakshi Temple"),
            Arrays.asList("Heritage Hotel"),
            Arrays.asList("Local messes")
        ));
        cities.put("Coimbatore", new CityInfo("Coimbatore", "India", "Tamil Nadu", 11.0168, 76.9558,
            "Industrial and textile city.", "Tropical", "Oct-Mar",
            76, 73, 35, "Industry and agriculture center.",
            Arrays.asList("VOC Park","Marudamalai Temple"),
            Arrays.asList("Hotel Vivanta"),
            Arrays.asList("Annapoorna")
        ));
        cities.put("Tiruchirappalli", new CityInfo("Tiruchirappalli", "India", "Tamil Nadu", 10.7905, 78.7047,
            "Historic city with temples.", "Tropical", "Oct-Mar",
            77, 71, 33, "Rockfort and temples.",
            Arrays.asList("Rockfort Temple"),
            Arrays.asList("Grand Garden"),
            Arrays.asList("Local restaurants")
        ));
        cities.put("Thanjavur", new CityInfo("Thanjavur", "India", "Tamil Nadu", 10.7867, 79.1378,
            "Known for Brihadeeswarar Temple.", "Tropical", "Oct-Mar",
            79, 68, 30, "Rich Chola architecture.",
            Arrays.asList("Brihadeeswarar Temple"),
            Arrays.asList("Heritage stay"),
            Arrays.asList("Local cuisine")
        ));

        // Maharashtra top cities
        cities.put("Mumbai", new CityInfo("Mumbai", "India", "Maharashtra", 19.0760, 72.8777,
            "Financial capital, dense metropolis.", "Tropical", "Nov-Feb",
            60, 72, 55, "Very large city; diverse attractions.",
            Arrays.asList("Gateway of India","Marine Drive"),
            Arrays.asList("Taj Mahal Palace","The Oberoi"),
            Arrays.asList("Bademiya","Leopold Cafe")
        ));
        cities.put("Pune", new CityInfo("Pune", "India", "Maharashtra", 18.5204, 73.8567,
            "Educational and IT hub.", "Tropical", "Nov-Feb",
            72, 76, 42, "Technology and education center.",
            Arrays.asList("Aga Khan Palace"),
            Arrays.asList("Conrad Pune"),
            Arrays.asList("Vaishali")
        ));
        cities.put("Nagpur", new CityInfo("Nagpur", "India", "Maharashtra", 21.1458, 79.0882,
            "Major city in central India.", "Tropical", "Nov-Feb",
            71, 68, 40,
            "Orange city.",
            Arrays.asList("Deekshabhoomi"),
            Arrays.asList("Ginger Nagpur"),
            Arrays.asList("Local eateries")
        ));
        cities.put("Nashik", new CityInfo("Nashik", "India", "Maharashtra", 20.0110, 73.7904,
            "Pilgrimage and wine region.", "Tropical", "Nov-Feb",
            74, 70, 38,
            "Religious and wine tourism.",
            Arrays.asList("Trimbakeshwar"),
            Arrays.asList("Ibis Nashik"),
            Arrays.asList("Local food")
        ));
        cities.put("Aurangabad", new CityInfo("Aurangabad", "India", "Maharashtra", 19.8762, 75.3433,
            "Gateway to Ajanta and Ellora caves.", "Tropical", "Nov-Feb",
            69, 65, 44,
            "History and caves.",
            Arrays.asList("Ajanta & Ellora"),
            Arrays.asList("The Fern"),
            Arrays.asList("Local cuisine")
        ));

        // Delhi top cities (NCR)
        cities.put("New Delhi", new CityInfo("New Delhi", "India", "Delhi", 28.6139, 77.2090,
            "National capital region.", "Humid subtropical", "Oct-Mar",
            65, 68, 58,
            "Capital city.",
            Arrays.asList("India Gate","Red Fort"),
            Arrays.asList("The Oberoi","Taj Mahal Hotel"),
            Arrays.asList("Khan Market","Paranthe Wali Gali")
        ));
        cities.put("Gurugram", new CityInfo("Gurugram", "India", "Delhi", 28.4595, 77.0266,
            "Corporate hub in NCR.", "Humid subtropical", "Oct-Mar",
            67, 70, 50,
            "IT and corporate offices.",
            Arrays.asList("Cyber Hub"),
            Arrays.asList("Leela Ambience"),
            Arrays.asList("Local malls")
        ));
        cities.put("Noida", new CityInfo("Noida", "India", "Delhi", 28.5355, 77.3910,
            "Residential and IT hub.", "Humid subtropical", "Oct-Mar",
            66, 69, 52,
            "Planned city in NCR.",
            Arrays.asList("Okhla Bird Sanctuary"),
            Arrays.asList("Radisson Blu"),
            Arrays.asList("Local food courts")
        ));
        cities.put("Faridabad", new CityInfo("Faridabad", "India", "Delhi", 28.4089, 77.3178,
            "Industrial city in NCR.", "Humid subtropical", "Oct-Mar",
            62, 62, 60,
            "Industrial and residential.",
            Arrays.asList("Surajkund"),
            Arrays.asList("Local hotels"),
            Arrays.asList("Local restaurants")
        ));
        cities.put("Ghaziabad", new CityInfo("Ghaziabad", "India", "Delhi", 28.6692, 77.4538,
            "Industrial/residential NCR city.", "Humid subtropical", "Oct-Mar",
            61, 63, 61,
            "Industrial city.",
            Arrays.asList("Local temples"),
            Arrays.asList("Local hotels"),
            Arrays.asList("Local eateries")
        ));

        // Kerala top cities
        cities.put("Thiruvananthapuram", new CityInfo("Thiruvananthapuram", "India", "Kerala", 8.5241, 76.9366,
            "State capital with beaches and temples.", "Tropical", "Nov-Feb",
            79, 75, 30,
            "Capital city of Kerala.",
            Arrays.asList("Kovalam Beach"),
            Arrays.asList("Taj Green Cove"),
            Arrays.asList("Local cuisine")
        ));
        cities.put("Kochi", new CityInfo("Kochi", "India", "Kerala", 9.9312, 76.2673,
            "Port city with backwaters.", "Tropical", "Oct-Feb",
            78, 74, 32,
            "Historic port city.",
            Arrays.asList("Fort Kochi"),
            Arrays.asList("Grand Hyatt Kochi"),
            Arrays.asList("Local seafood")
        ));
        cities.put("Kozhikode", new CityInfo("Kozhikode", "India", "Kerala", 11.2588, 75.7804,
            "Historic trading city.", "Tropical", "Oct-Feb",
            76, 72, 35,
            "Known for sweet meats and beaches.",
            Arrays.asList("Kappad Beach"),
            Arrays.asList("Local hotels"),
            Arrays.asList("Local eateries")
        ));
        cities.put("Kollam", new CityInfo("Kollam", "India", "Kerala", 8.8932, 76.6141,
            "Backwater and port city.", "Tropical", "Oct-Feb",
            77, 71, 34,
            "Backwater tourism.",
            Arrays.asList("Ashtamudi Lake"),
            Arrays.asList("Local resorts"),
            Arrays.asList("Local seafood")
        ));
        cities.put("Alappuzha", new CityInfo("Alappuzha", "India", "Kerala", 9.4981, 76.3388,
            "Backwater town known for houseboats.", "Tropical", "Oct-Feb",
            80, 69, 30,
            "Houseboat tourism.",
            Arrays.asList("Alappuzha Backwaters"),
            Arrays.asList("Houseboat stays"),
            Arrays.asList("Local cuisine")
        ));

        // West Bengal top cities
        cities.put("Kolkata", new CityInfo("Kolkata", "India", "West Bengal", 22.5726, 88.3639,
            "Cultural capital of East India.", "Tropical wet and dry", "Oct-Feb",
            64, 67, 50,
            "Historic colonial city.",
            Arrays.asList("Victoria Memorial","Howrah Bridge"),
            Arrays.asList("The Oberoi Grand"),
            Arrays.asList("6 Ballygunge Place")
        ));
        cities.put("Darjeeling", new CityInfo("Darjeeling", "India", "West Bengal", 27.0360, 88.2627,
            "Hill station known for tea gardens.", "Subtropical highland", "Mar-May",
            83, 66, 28,
            "Scenic hill station.",
            Arrays.asList("Tiger Hill"),
            Arrays.asList("Local hotels"),
            Arrays.asList("Local cafes")
        ));
        cities.put("Siliguri", new CityInfo("Siliguri", "India", "West Bengal", 26.7271, 88.3953,
            "Gateway to Northeast India.", "Subtropical", "Oct-Mar",
            72, 68, 36,
            "Transit hub.",
            Arrays.asList("Coronation Bridge"),
            Arrays.asList("Local hotels"),
            Arrays.asList("Local food")
        ));
        cities.put("Durgapur", new CityInfo("Durgapur", "India", "West Bengal", 23.5204, 87.3119,
            "Industrial city.", "Tropical", "Oct-Feb",
            70, 65, 40,
            "Industrial center.",
            Arrays.asList("Buddha Park"),
            Arrays.asList("Local hotels"),
            Arrays.asList("Local cuisine")
        ));
        cities.put("Asansol", new CityInfo("Asansol", "India", "West Bengal", 23.6806, 86.9855,
            "Coal and steel city.", "Tropical", "Oct-Feb",
            68, 64, 42,
            "Industrial city.",
            Arrays.asList("Local temples"),
            Arrays.asList("Local hotels"),
            Arrays.asList("Local eateries")
        ));

        // Rajasthan top cities
        cities.put("Jaipur", new CityInfo("Jaipur", "India", "Rajasthan", 26.9124, 75.7873,
            "Pink City and popular tourist destination.", "Arid", "Oct-Mar",
            73, 69, 38,
            "Historical forts and palaces.",
            Arrays.asList("Hawa Mahal","Amber Fort"),
            Arrays.asList("Rambagh Palace"),
            Arrays.asList("Local cuisine")
        ));
        cities.put("Jodhpur", new CityInfo("Jodhpur", "India", "Rajasthan", 26.2389, 73.0243,
            "Blue City near Mehrangarh Fort.", "Arid", "Oct-Mar",
            74, 67, 37,
            "Fort and desert tourism.",
            Arrays.asList("Mehrangarh Fort"),
            Arrays.asList("RAAS Jodhpur"),
            Arrays.asList("Local eateries")
        ));
        cities.put("Udaipur", new CityInfo("Udaipur", "India", "Rajasthan", 24.5854, 73.7125,
            "Lake city, romantic destination.", "Arid", "Oct-Mar",
            76, 75, 33,
            "Lakes and palaces.",
            Arrays.asList("Lake Pichola","City Palace"),
            Arrays.asList("The Leela Palace"),
            Arrays.asList("Local cuisine")
        ));
        cities.put("Bikaner", new CityInfo("Bikaner", "India", "Rajasthan", 28.0229, 73.3119,
            "Desert city with forts.", "Arid", "Oct-Mar",
            70, 60, 44,
            "Camel festival and forts.",
            Arrays.asList("Junagarh Fort"),
            Arrays.asList("Local hotels"),
            Arrays.asList("Local food")
        ));
        cities.put("Ajmer", new CityInfo("Ajmer", "India", "Rajasthan", 26.4499, 74.6399,
            "Pilgrimage city.", "Arid", "Oct-Mar",
            72, 63, 41,
            "Dargah Sharif and history.",
            Arrays.asList("Ajmer Sharif"),
            Arrays.asList("Local hotels"),
            Arrays.asList("Local eateries")
        ));

        // Uttar Pradesh top cities
        cities.put("Lucknow", new CityInfo("Lucknow", "India", "Uttar Pradesh", 26.8467, 80.9462,
            "Capital of UP, cultural city.", "Humid subtropical", "Oct-Mar",
            68, 66, 48,
            "Historical and culinary city.",
            Arrays.asList("Bara Imambara"),
            Arrays.asList("Vivanta Lucknow"),
            Arrays.asList("Tunday Kababi")
        ));
        cities.put("Kanpur", new CityInfo("Kanpur", "India", "Uttar Pradesh", 26.4499, 80.3319,
            "Industrial city.", "Humid subtropical", "Oct-Mar",
            65, 62, 52,
            "Industrial centre.",
            Arrays.asList("Allen Forest Zoo"),
            Arrays.asList("Local hotels"),
            Arrays.asList("Local cuisine")
        ));
        cities.put("Varanasi", new CityInfo("Varanasi", "India", "Uttar Pradesh", 25.3176, 82.9739,
            "Ancient spiritual city on Ganges.", "Humid subtropical", "Oct-Mar",
            77, 60, 35,
            "Pilgrimage and ghats.",
            Arrays.asList("Kashi Vishwanath Temple"),
            Arrays.asList("Local guesthouses"),
            Arrays.asList("Local food")
        ));
        cities.put("Agra", new CityInfo("Agra", "India", "Uttar Pradesh", 27.1767, 78.0081,
            "Home of the Taj Mahal.", "Humid subtropical", "Oct-Mar",
            69, 64, 46,
            "Tourism hub.",
            Arrays.asList("Taj Mahal"),
            Arrays.asList("The Oberoi Amarvilas"),
            Arrays.asList("Local eateries")
        ));
        cities.put("Prayagraj", new CityInfo("Prayagraj", "India", "Uttar Pradesh", 25.4358, 81.8463,
            "Confluence of rivers, pilgrimage.", "Humid subtropical", "Oct-Mar",
            67, 61, 49,
            "Kumbh and religious tourism.",
            Arrays.asList("Triveni Sangam"),
            Arrays.asList("Local hotels"),
            Arrays.asList("Local food")
        ));

        // Telangana
        cities.put("Hyderabad", new CityInfo("Hyderabad", "India", "Telangana", 17.3850, 78.4867,
            "Tech and historic city.", "Tropical", "Oct-Feb",
            74, 78, 36,
            "Charminar and IT corridor.",
            Arrays.asList("Charminar","Golconda Fort"),
            Arrays.asList("Taj Falaknuma"),
            Arrays.asList("Paradise Biryani")
        ));
        cities.put("Warangal", new CityInfo("Warangal", "India", "Telangana", 17.9689, 79.5941,
            "Historic city.", "Tropical", "Oct-Feb",
            76, 65, 34,
            "Kakatiya monuments.",
            Arrays.asList("Warangal Fort"),
            Arrays.asList("Local hotels"),
            Arrays.asList("Local food")
        ));
        cities.put("Nizamabad", new CityInfo("Nizamabad", "India", "Telangana", 18.6726, 78.0941,
            "Regional city.", "Tropical", "Oct-Feb",
            71, 63, 38,
            "Local trade center.",
            Arrays.asList("Local temples"),
            Arrays.asList("Local hotels"),
            Arrays.asList("Local eateries")
        ));
        cities.put("Karimnagar", new CityInfo("Karimnagar", "India", "Telangana", 18.4386, 79.1288,
            "Regional town.", "Tropical", "Oct-Feb",
            70, 62, 39,
            "Local culture.",
            Arrays.asList("Local sights"),
            Arrays.asList("Local hotels"),
            Arrays.asList("Local cuisine")
        ));
        cities.put("Khammam", new CityInfo("Khammam", "India", "Telangana", 17.2473, 80.1514,
            "Regional city.", "Tropical", "Oct-Feb",
            69, 61, 40,
            "Local tourism.",
            Arrays.asList("Local sights"),
            Arrays.asList("Local hotels"),
            Arrays.asList("Local eateries")
        ));

        // Gujarat
        cities.put("Ahmedabad", new CityInfo("Ahmedabad", "India", "Gujarat", 23.0225, 72.5714,
            "Largest city in Gujarat.", "Tropical", "Oct-Feb",
            72, 73, 39,
            "Historic and business hub.",
            Arrays.asList("Sabarmati Ashram"),
            Arrays.asList("Hyatt Ahmedabad"),
            Arrays.asList("Manek Chowk")
        ));
        cities.put("Surat", new CityInfo("Surat", "India", "Gujarat", 21.1702, 72.8311,
            "Diamond and textile hub.", "Tropical", "Oct-Feb",
            74, 70, 36,
            "Commerce and industry.",
            Arrays.asList("Dutch Garden"),
            Arrays.asList("Local hotels"),
            Arrays.asList("Local cuisine")
        ));
        cities.put("Vadodara", new CityInfo("Vadodara", "India", "Gujarat", 22.3072, 73.1812,
            "Cultural city.", "Tropical", "Oct-Feb",
            73, 69, 37,
            "Palaces and museums.",
            Arrays.asList("Laxmi Vilas Palace"),
            Arrays.asList("Local hotels"),
            Arrays.asList("Local food")
        ));
        cities.put("Rajkot", new CityInfo("Rajkot", "India", "Gujarat", 22.3039, 70.8022,
            "Regional city.", "Tropical", "Oct-Feb",
            71, 66, 38,
            "Local history.",
            Arrays.asList("Mahatma Gandhi High School"),
            Arrays.asList("Local hotels"),
            Arrays.asList("Local eateries")
        ));
        cities.put("Bhavnagar", new CityInfo("Bhavnagar", "India", "Gujarat", 21.7645, 72.1519,
            "Port and regional city.", "Tropical", "Oct-Feb",
            70, 65, 40,
            "Port activities.",
            Arrays.asList("Takhteshwar Temple"),
            Arrays.asList("Local hotels"),
            Arrays.asList("Local cuisine")
        ));

        // --- top cities mapping (first 5 used) ---
        topCitiesByState.put("Karnataka", Arrays.asList("Bengaluru","Mysore","Mangalore","Hubli","Belgaum"));
        topCitiesByState.put("Tamil Nadu", Arrays.asList("Chennai","Madurai","Coimbatore","Tiruchirappalli","Thanjavur"));
        topCitiesByState.put("Maharashtra", Arrays.asList("Mumbai","Pune","Nagpur","Nashik","Aurangabad"));
        topCitiesByState.put("Delhi", Arrays.asList("New Delhi","Gurugram","Noida","Faridabad","Ghaziabad"));
        topCitiesByState.put("Kerala", Arrays.asList("Thiruvananthapuram","Kochi","Kozhikode","Kollam","Alappuzha"));
        topCitiesByState.put("West Bengal", Arrays.asList("Kolkata","Darjeeling","Siliguri","Durgapur","Asansol"));
        topCitiesByState.put("Rajasthan", Arrays.asList("Jaipur","Jodhpur","Udaipur","Bikaner","Ajmer"));
        topCitiesByState.put("Uttar Pradesh", Arrays.asList("Lucknow","Kanpur","Varanasi","Agra","Prayagraj"));
        topCitiesByState.put("Telangana", Arrays.asList("Hyderabad","Warangal","Nizamabad","Karimnagar","Khammam"));
        topCitiesByState.put("Gujarat", Arrays.asList("Ahmedabad","Surat","Vadodara","Rajkot","Bhavnagar"));
    }

    public static CityInfo getCity(String key) {
        return cities.get(key);
    }

    public static List<CityInfo> getTopCitiesForState(String stateKey) {
        // if a method with this name already exists and returns List<String>, this override ensures
        // callers that need CityInfo objects get a proper List<CityInfo>.
        List<String> names = topCitiesByState.getOrDefault(stateKey, Collections.emptyList());
        List<CityInfo> list = new ArrayList<>();
        for (String n : names) {
            CityInfo ci = cities.get(n);
            if (ci != null) list.add(ci);
        }
        return list;
    }

    public static class CityInfo {
        public String name, country, region;
        public double latitude, longitude;
        public String description;
        public String climate, bestTimeToVisit;
        public int safetyIndex, qualityOfLife, crimeIndex;
        public String history;
        public List<String> attractions, hotels, restaurants;
        
        public CityInfo(String name, String country, String region,
                       double lat, double lon, String description,
                       String climate, String bestTime,
                       int safety, int quality, int crime,
                       String history, List<String> attractions,
                       List<String> hotels, List<String> restaurants) {
            this.name = name;
            this.country = country;
            this.region = region;
            this.latitude = lat;
            this.longitude = lon;
            this.description = description;
            this.climate = climate;
            this.bestTimeToVisit = bestTime;
            this.safetyIndex = safety;
            this.qualityOfLife = quality;
            this.crimeIndex = crime;
            this.history = history;
            this.attractions = attractions;
            this.hotels = hotels;
            this.restaurants = restaurants;
        }
    }
}
