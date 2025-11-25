package Database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Model.User.Applicant;
import Model.User.Landlord;
import Model.User.Tenant;
import Model.User.User;
import Model.Property.Room;
import Enums.RoomPricingType;
import Enums.RoomStatus;
import Enums.RoomType;

public class DatabaseManagement {

    private static final String RELATIVE_USERS_PATH = "src" + File.separator + "Data" + File.separator + "Users.json";
    private static final String RELATIVE_ROOMS_PATH = "src" + File.separator + "Data" + File.separator + "Rooms.json";
    private static final String RELATIVE_CONTRACTS_PATH = "src" + File.separator + "Data" + File.separator
            + "Contracts.json";

    /**
     * Initialize database: ensure files exist and load data into memory.
     */
    public static void init() {
        try {
            ensureUsersFileExists();
            ensureRoomsFileExists();
            ensureContractsFileExists();
            loadUsers();
            loadContracts();
        } catch (Exception e) {
            System.out.println("Warning: failed to initialize database: " + e.getMessage());
        }
    }

    /**
     * Load contracts and attach active contracts to tenant objects and set room
     * statuses accordingly
     */
    public static void loadContracts() {
        LinkedList<Model.Contract.Contract> contracts = getContracts();

        // Attach active contracts to tenant objects in memory and ensure rooms are
        // marked
        for (Model.Contract.Contract c : contracts) {
            Model.User.Tenant tenant = c.getTenantID();
            if (tenant != null) {
                // find the tenant instance from memory (loaded by loadUsers)
                for (Model.User.User u : Model.User.User.getUsers()) {
                    if (u instanceof Model.User.Tenant) {
                        Model.User.Tenant t = (Model.User.Tenant) u;
                        if (tenant != null && t.getTenantID().equals(tenant.getTenantID())) {
                            // if contract is active, attach it
                            if (c.getContractStatus() == Enums.ContractStatus.Active) {
                                t.setContract(c);
                            }
                            break;
                        }
                    }
                }
            }

            // update the room status to occupied for active contracts
            if (c.getRoomID() != null && c.getContractStatus() == Enums.ContractStatus.Active) {
                String rid = c.getRoomID().getRoomID();
                for (Model.Property.Room r : getRooms()) {
                    if (r.getRoomID().equals(rid)) {
                        r.setStatus(Enums.RoomStatus.Occupied);
                        break;
                    }
                }
            }
        }

        // persist rooms status changes if any
        saveRooms(getRooms());
    }

    private static void ensureUsersFileExists() throws IOException {
        File f = getUsersFile();
        File parent = f.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        if (!f.exists()) {
            try (BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8))) {
                bw.write("{\n  \"users\": []\n}");
            }
        }
    }

    private static void ensureRoomsFileExists() throws IOException {
        File f = getRoomsFile();
        File parent = f.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        if (!f.exists()) {
            try (BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8))) {
                bw.write("{\n  \"rooms\": []\n}");
            }
        }
    }

    // ==================== ROOM MANAGEMENT METHODS ====================
    /**
     * Load rooms from `src/Data/Rooms.json`
     */
    public static LinkedList<Room> getRooms() {
        LinkedList<Room> rooms = new LinkedList<>();
        File f = getRoomsFile();
        if (!f.exists()) {
            try {
                ensureRoomsFileExists();
            } catch (IOException e) {
                System.out.println("Error creating rooms file: " + e.getMessage());
                return rooms;
            }
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (Exception e) {
            System.out.println("Error reading rooms file: " + e.getMessage());
            return rooms;
        }

        String content = sb.toString();
        // find the rooms array
        int start = content.indexOf('[');
        int end = content.lastIndexOf(']');
        if (start == -1 || end == -1 || end <= start) {
            return rooms; // nothing to load
        }

        String array = content.substring(start + 1, end).trim();
        if (array.isEmpty()) {
            return rooms;
        }

        // split objects by '},' boundary but keep braces
        List<String> objects = splitJsonObjects(array);

        for (String obj : objects) {
            try {
                String roomID = extractString(obj, "roomID");
                String roomNumber = extractString(obj, "roomNumber");
                String roomTypeStr = extractString(obj, "roomType");
                double price = extractDouble(obj, "price", 0.0);
                int capacity = extractInt(obj, "capacity", 1);
                String pricingTypeStr = extractString(obj, "pricingType");
                String statusStr = extractString(obj, "status");

                // Parse enums
                RoomType roomType = RoomType.Single;
                if (roomTypeStr != null) {
                    try {
                        roomType = RoomType.valueOf(roomTypeStr);
                    } catch (Exception ignored) {
                    }
                }

                RoomPricingType pricingType = RoomPricingType.per_head;
                if (pricingTypeStr != null) {
                    try {
                        pricingType = RoomPricingType.valueOf(pricingTypeStr);
                    } catch (Exception ignored) {
                    }
                }

                RoomStatus status = RoomStatus.Vacant;
                if (statusStr != null) {
                    try {
                        status = RoomStatus.valueOf(statusStr);
                    } catch (Exception ignored) {
                    }
                }

                Room room = new Room(capacity, price, pricingType, roomID, roomNumber, status, roomType);
                rooms.add(room);

            } catch (Exception e) {
                System.out.println("Error parsing room object: " + e.getMessage());
            }
        }

        return rooms;
    }

    // ==================== CONTRACT MANAGEMENT METHODS ====================
    private static void ensureContractsFileExists() throws java.io.IOException {
        File f = getContractsFile();
        File parent = f.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        if (!f.exists()) {
            try (BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8))) {
                bw.write("{\n  \"contracts\": []\n}");
            }
        }
    }

    /**
     * Load contracts from `src/Data/Contracts.json`
     */
    public static LinkedList<Model.Contract.Contract> getContracts() {
        LinkedList<Model.Contract.Contract> contracts = new LinkedList<>();
        File f = getContractsFile();
        if (!f.exists()) {
            try {
                ensureContractsFileExists();
            } catch (IOException e) {
                System.out.println("Error creating contracts file: " + e.getMessage());
                return contracts;
            }
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (Exception e) {
            System.out.println("Error reading contracts file: " + e.getMessage());
            return contracts;
        }

        String content = sb.toString();
        int start = content.indexOf('[');
        int end = content.lastIndexOf(']');
        if (start == -1 || end == -1 || end <= start) {
            return contracts; // nothing to load
        }

        String array = content.substring(start + 1, end).trim();
        if (array.isEmpty()) {
            return contracts;
        }

        List<String> objects = splitJsonObjects(array);

        for (String obj : objects) {
            try {
                String contractID = extractString(obj, "contractID");
                String tenantID = extractString(obj, "tenantID");
                String roomID = extractString(obj, "roomID");
                long startMillis = extractLong(obj, "startDate", -1L);
                long endMillis = extractLong(obj, "endDate", -1L);
                double monthlyRent = extractDouble(obj, "monthlyRent", 0.0);
                double deposit = extractDouble(obj, "deposit", 0.0);
                String statusStr = extractString(obj, "status");
                String termReason = extractString(obj, "terminationReason");

                Enums.ContractStatus status = Enums.ContractStatus.Active;
                if (statusStr != null) {
                    try {
                        status = Enums.ContractStatus.valueOf(statusStr);
                    } catch (Exception ignored) {
                    }
                }

                Model.User.Tenant tenant = null;
                if (tenantID != null) {
                    for (Model.User.User u : Model.User.User.getUsers()) {
                        if (u instanceof Model.User.Tenant) {
                            Model.User.Tenant t = (Model.User.Tenant) u;
                            if (t.getTenantID().equals(tenantID)) {
                                tenant = t;
                                break;
                            }
                        }
                    }
                }

                Model.Property.Room room = null;
                if (roomID != null) {
                    for (Model.Property.Room r : getRooms()) {
                        if (r.getRoomID().equals(roomID)) {
                            room = r;
                            break;
                        }
                    }
                }

                java.util.Date startDate = startMillis > 0 ? new java.util.Date(startMillis) : null;
                java.util.Date endDate = endMillis > 0 ? new java.util.Date(endMillis) : null;

                Model.Contract.Contract c = new Model.Contract.Contract(contractID, tenant, room, startDate, endDate,
                        monthlyRent, deposit, status);
                c.setTerminationReason(termReason);
                contracts.add(c);

            } catch (Exception e) {
                System.out.println("Error parsing contract object: " + e.getMessage());
            }
        }

        return contracts;
    }

    /**
     * Save contracts to `src/Data/Contracts.json`
     */
    public static void saveContracts(LinkedList<Model.Contract.Contract> contracts) {
        File f = getContractsFile();
        try {
            ensureContractsFileExists();
        } catch (IOException e) {
            System.out.println("Error ensuring contracts file: " + e.getMessage());
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{\n  \"contracts\": [\n");

        for (int i = 0; i < contracts.size(); i++) {
            Model.Contract.Contract c = contracts.get(i);
            sb.append("    {");
            sb.append("\"contractID\": \"").append(escape(c.getContractID())).append("\",");
            sb.append(" \"tenantID\": \"").append(escape(c.getTenantID() != null ? c.getTenantID().getTenantID() : ""))
                    .append("\",");
            sb.append(" \"roomID\": \"").append(escape(c.getRoomID() != null ? c.getRoomID().getRoomID() : ""))
                    .append("\",");
            sb.append(" \"startDate\": ").append(c.getStartDate() != null ? c.getStartDate().getTime() : 0).append(",");
            sb.append(" \"endDate\": ").append(c.getEndDate() != null ? c.getEndDate().getTime() : 0).append(",");
            sb.append(" \"monthlyRent\": ").append(c.getMonthlyRent()).append(",");
            sb.append(" \"deposit\": ").append(c.getDeposit()).append(",");
            sb.append(" \"status\": \"").append(c.getContractStatus() != null ? c.getContractStatus().name() : "Active")
                    .append("\"");
            if (c.getTerminationReason() != null) {
                sb.append(", \"terminationReason\": \"").append(escape(c.getTerminationReason())).append("\"");
            }
            sb.append(" }");
            if (i < contracts.size() - 1) {
                sb.append(",\n"); 
            }else {
                sb.append('\n');
            }
        }

        sb.append("  ]\n}");

        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(f, false), StandardCharsets.UTF_8))) {
            bw.write(sb.toString());
        } catch (Exception e) {
            System.out.println("Error writing contracts file: " + e.getMessage());
        }
    }

    private static long extractLong(String json, String key, long defaultVal) {
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*([0-9]+)");
        Matcher m = p.matcher(json);
        if (m.find()) {
            try {
                return Long.parseLong(m.group(1));
            } catch (Exception e) {
                return defaultVal;
            }
        }
        return defaultVal;
    }

    /**
     * Save rooms to `src/Data/Rooms.json`
     */
    public static void saveRooms(LinkedList<Room> rooms) {
        File f = getRoomsFile();
        try {
            ensureRoomsFileExists();
        } catch (IOException e) {
            System.out.println("Error ensuring rooms file: " + e.getMessage());
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{\n  \"rooms\": [\n");

        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            sb.append("    {");
            sb.append("\"roomID\": \"").append(escape(room.getRoomID())).append("\",");
            sb.append(" \"roomNumber\": \"").append(escape(room.getRoomNumber())).append("\",");
            sb.append(" \"roomType\": \"").append(room.getType() != null ? room.getType().name() : "Single")
                    .append("\",");
            sb.append(" \"price\": ").append(room.getPrice()).append(",");
            sb.append(" \"capacity\": ").append(room.getCapacity()).append(",");
            sb.append(" \"pricingType\": \"")
                    .append(room.getPricingType() != null ? room.getPricingType().name() : "per_head").append("\",");
            sb.append(" \"status\": \"").append(room.getStatus() != null ? room.getStatus().name() : "Vacant")
                    .append("\"");
            sb.append(" }");
            if (i < rooms.size() - 1) {
                sb.append(",\n"); 
            }else {
                sb.append('\n');
            }
        }

        sb.append("  ]\n}");

        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(f, false), StandardCharsets.UTF_8))) {
            bw.write(sb.toString());
        } catch (Exception e) {
            System.out.println("Error writing rooms file: " + e.getMessage());
        }
    }

    private static int extractInt(String json, String key, int defaultVal) {
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*([0-9]+)");
        Matcher m = p.matcher(json);
        if (m.find()) {
            try {
                return Integer.parseInt(m.group(1));
            } catch (Exception e) {
                return defaultVal;
            }
        }
        return defaultVal;
    }

    // ==================== USER MANAGEMENT METHODS (existing) ====================
    /**
     * Load users from `src/Data/Users.json` into User.getUsers()
     */
    public static void loadUsers() {
        File f = getUsersFile();
        if (!f.exists()) {
            try {
                ensureUsersFileExists();
            } catch (IOException e) {
                System.out.println("Error creating users file: " + e.getMessage());
                return;
            }
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (Exception e) {
            System.out.println("Error reading users file: " + e.getMessage());
            return;
        }

        String content = sb.toString();
        // find the users array
        int start = content.indexOf('[');
        int end = content.lastIndexOf(']');
        if (start == -1 || end == -1 || end <= start) {
            return; // nothing to load
        }

        String array = content.substring(start + 1, end).trim();
        if (array.isEmpty()) {
            return;
        }

        // split objects by '},' boundary but keep braces
        List<String> objects = splitJsonObjects(array);

        for (String obj : objects) {
            String userID = extractString(obj, "userID");
            String username = extractString(obj, "username");
            String password = extractString(obj, "password");
            String firstName = extractString(obj, "firstName");
            String lastName = extractString(obj, "lastName");
            String contactNumber = extractString(obj, "contactNumber");
            String roleStr = extractString(obj, "role");

            User.Role role = User.Role.APPLICANT;
            if (roleStr != null) {
                try {
                    role = User.Role.valueOf(roleStr);
                } catch (Exception ignored) {
                }
            }

            try {
                switch (role) {
                    case LANDLORD -> {
                        Landlord l = new Landlord(contactNumber, firstName, lastName, password, userID, username,
                                User.Role.LANDLORD);
                        User.getUsers().add(l);
                    }
                    case TENANT -> {
                        String tenantID = extractString(obj, "tenantID");
                        String roomID = extractString(obj, "roomID");
                        String emergencyContact = extractString(obj, "emergencyContact");
                        double balance = extractDouble(obj, "balance", 0.0);
                        // REMOVED: String idNumber = extractString(obj, "idNumber");
                        Tenant t = new Tenant(contactNumber, firstName, lastName, password, userID, username,
                                User.Role.TENANT, tenantID == null ? userID : tenantID, roomID, null, balance,
                                emergencyContact); // REMOVED: idNumber parameter
                        User.getUsers().add(t);
                    }
                    default -> {
                        Applicant a = new Applicant(contactNumber, firstName, lastName, password, userID,
                                username, User.Role.APPLICANT);
                        User.getUsers().add(a);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error instantiating user " + username + ": " + e.getMessage());
            }
        }
    }

    private static List<String> splitJsonObjects(String arrayContent) {
        List<String> out = new ArrayList<>();
        int brace = 0;
        int last = 0;
        for (int i = 0; i < arrayContent.length(); i++) {
            char c = arrayContent.charAt(i);
            if (c == '{') {
                if (brace == 0) {
                    last = i;
                }
                brace++;
            } else if (c == '}') {
                brace--;
                if (brace == 0) {
                    out.add(arrayContent.substring(last, i + 1).trim());
                }
            }
        }
        return out;
    }

    private static String extractString(String json, String key) {
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\"([^\"]*)\"");
        Matcher m = p.matcher(json);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    private static double extractDouble(String json, String key, double defaultVal) {
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*([0-9.+-eE]+)");
        Matcher m = p.matcher(json);
        if (m.find()) {
            try {
                return Double.parseDouble(m.group(1));
            } catch (Exception e) {
                return defaultVal;
            }
        }
        return defaultVal;
    }

    /**
     * Save current users (User.getUsers()) into `src/Data/Users.json`.
     */
    public static void saveUsers() {
        File f = getUsersFile();
        try {
            ensureUsersFileExists();
        } catch (IOException e) {
            System.out.println("Error ensuring users file: " + e.getMessage());
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{\n  \"users\": [\n");

        List<User> users = User.getUsers();
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            sb.append("    {");
            sb.append("\"userID\": \"").append(escape(u.getUserID())).append("\",");
            sb.append(" \"username\": \"").append(escape(u.getUsername())).append("\",");
            sb.append(" \"password\": \"").append(escape(u.getPassword())).append("\",");
            sb.append(" \"firstName\": \"").append(escape(u.getFirstName())).append("\",");
            sb.append(" \"lastName\": \"").append(escape(u.getLastName())).append("\",");
            sb.append(" \"contactNumber\": \"").append(escape(u.getContactNumber())).append("\",");
            sb.append(" \"role\": \"").append(u.getRole() != null ? u.getRole().name() : "APPLICANT").append("\"");

            if (u instanceof Tenant) {
                Tenant t = (Tenant) u;
                sb.append(", \"tenantID\": \"").append(escape(t.getTenantID())).append("\"");
                sb.append(", \"roomID\": \"").append(escape(t.getRoomID())).append("\"");
                sb.append(", \"balance\": ").append(t.getBalance());
                sb.append(", \"emergencyContact\": \"").append(escape(t.getEmergencyContact())).append("\"");
            }

            sb.append(" }");
            if (i < users.size() - 1) {
                sb.append(",\n"); 
            }else {
                sb.append('\n');
            }
        }

        sb.append("  ]\n}");

        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(f, false), StandardCharsets.UTF_8))) {
            bw.write(sb.toString());
        } catch (Exception e) {
            System.out.println("Error writing users file: " + e.getMessage());
        }
    }

    private static String escape(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }

    private static File getUsersFile() {
        try {
            File cwd = new File(".").getCanonicalFile();
            File dir = cwd;
            // search upward for a folder containing `src`
            for (int i = 0; i < 10 && dir != null; i++) {
                File src = new File(dir, "src");
                if (src.exists() && src.isDirectory()) {
                    return new File(src, "Data" + File.separator + "Users.json");
                }
                dir = dir.getParentFile();
            }
        } catch (IOException ignored) {
        }
        // fallback to relative path
        return new File(RELATIVE_USERS_PATH);
    }

    private static File getRoomsFile() {
        try {
            File cwd = new File(".").getCanonicalFile();
            File dir = cwd;
            // search upward for a folder containing `src`
            for (int i = 0; i < 10 && dir != null; i++) {
                File src = new File(dir, "src");
                if (src.exists() && src.isDirectory()) {
                    return new File(src, "Data" + File.separator + "Rooms.json");
                }
                dir = dir.getParentFile();
            }
        } catch (IOException ignored) {
        }
        // fallback to relative path
        return new File(RELATIVE_ROOMS_PATH);
    }

    private static File getContractsFile() {
        try {
            File cwd = new File(".").getCanonicalFile();
            File dir = cwd;
            for (int i = 0; i < 10 && dir != null; i++) {
                File src = new File(dir, "src");
                if (src.exists() && src.isDirectory()) {
                    return new File(src, "Data" + File.separator + "Contracts.json");
                }
                dir = dir.getParentFile();
            }
        } catch (IOException ignored) {
        }
        return new File(RELATIVE_CONTRACTS_PATH);
    }

    /**
     * Add a user to memory and persist immediately.
     */
    public static void addUser(User u) {
        User.getUsers().add(u);
        saveUsers();
    }
}
