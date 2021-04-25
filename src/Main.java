import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class Main {

    private static HashMap<Integer, Employee> employees = new HashMap<Integer, Employee>();
    private static HashMap<Integer, Boolean> isChecked = new HashMap<Integer, Boolean>();
    private static List<Employee> empList = new ArrayList<Employee>();


    private static void loadEmployees() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter input file path:");
            String text = in.readLine();
            FileInputStream file = new FileInputStream(text);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                String[] values = data.split(", ");

                Date todayDate = Calendar.getInstance().getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String todayString = formatter.format(todayDate);

                if (values[3].equalsIgnoreCase("NULL")) {
                    values[3] = todayString;
                }

                int empId = Integer.parseInt(values[0]);
                int projectId = Integer.parseInt(values[1]);
                LocalDate from = LocalDate.parse(values[2]);
                LocalDate to = LocalDate.parse(values[3]);

                Employee employee;
                if (employees.containsKey(empId)) {
                    employee = employees.get(empId);
                } else {
                    employee = new Employee(empId);
                    employees.put(empId, employee);
                    isChecked.put(empId, false);
                }

                employee.addProject(projectId, from, to);

            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createDataStructure() {
        for (Map.Entry<Integer, Employee> employee : employees.entrySet()) {
            empList.add(employee.getValue());
        }

        for (int i = 0; i < empList.size(); i++) {
            Employee e = empList.get(i);
            for (int j = 0; j < empList.size() - 1; j++) {
                if (i == j) {
                    continue;
                }
                Employee s = empList.get(j);
                e.setSameProjects(s.getProjects(), s.getEmpId());
                s.setSameProjects(e.getProjects(), e.getEmpId());
            }
        }

        empList.forEach(Employee::setColleagueHours);
    }

    private static void showTopEmployees() {
        Set<String> list = new TreeSet<String>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String[] d1 = o1.split(",");
                String[] d2 = o2.split(",");
                long day1 = Long.parseLong(d1[2]);
                long day2 = Long.parseLong(d2[2]);

                if (day1 > day2) {
                    return -1;
                } else if (day2 > day1) {
                    return 1;
                }

                return 0;
            }
        });

        empList.forEach(e -> {
            var bestCollegue = e.getBestColleague();
            int currentEmpId = e.getEmpId();
            int collegueId = bestCollegue.getKey();
            long days = bestCollegue.getValue();
            if (currentEmpId > collegueId) {
                int tmp = currentEmpId;
                currentEmpId = collegueId;
                collegueId = tmp;
            }
            list.add(currentEmpId + "," + collegueId + "," + days);
        });

        String result = list.stream().findFirst().get();
        String[] splitted = result.split(",");

        if (splitted[2].equals("0")) {
            System.out.println("There is no pair of employees who have worked together on common projects.");
        } else {
            System.out.println("Best employees: employee with id: " + splitted[0] + " worked with employee with id: " + splitted[1] + " for " + splitted[2] + " days");
        }
    }

    public static void main(String[] args) {
        loadEmployees();
        createDataStructure();
        showTopEmployees();
    }
}
