import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Employee {

    private int empId;
    private List<ProjectInfo> projects;
    private HashMap<Integer, List<ProjectInfo>> sameProjects;
    private HashMap<Integer, Long> colleagueWorkingDays;

    Employee(int empId) {
        this.empId = empId;
        this.projects = new ArrayList<ProjectInfo>();
        this.sameProjects = new HashMap<Integer, List<ProjectInfo>>();
        this.colleagueWorkingDays = new HashMap<Integer, Long>();
    }

    int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    void addProject(int projectId, LocalDate from, LocalDate to) {
        this.projects.add(new ProjectInfo(projectId, from, to));
    }

    List<ProjectInfo> getProjects() {
        return this.projects;
    }

    public HashMap<Integer, List<ProjectInfo>> getSameProjects() {
        return this.sameProjects;
    }

    void setSameProjects(List<ProjectInfo> otherEmployeeProjects, int empId) {
        for (ProjectInfo p : otherEmployeeProjects) {

            if (this.projects
                    .stream()
                    .filter(iP -> iP.projectId == p.projectId && DateHelper.hasTimeOverlap(iP.from, iP.to, p.from, p.to))
                    .collect(Collectors.toList())
                    .size() > 0
            ) {
                if (!this.sameProjects.containsKey(empId)) {
                    this.sameProjects.put(empId, new ArrayList<ProjectInfo>());
                }

                if (this.sameProjects
                        .get(empId)
                        .stream()
                        .filter(iP -> iP.projectId == p.projectId)
                        .collect(Collectors.toList())
                        .size() == 0) {
                    this.sameProjects.get(empId).add(p);
                }
            }
        }
    }

    public HashMap<Integer, Long> getColleagueHours() {
        return colleagueWorkingDays;
    }

    void setColleagueHours() {
        for (Map.Entry<Integer, List<ProjectInfo>> entry : sameProjects.entrySet()) {
            int empId = entry.getKey();
            var matchedProjects = entry.getValue();
            this.colleagueWorkingDays.put(empId, 0L);
            matchedProjects
                    .forEach(project -> {
                        ProjectInfo thisProject = this.projects
                                .stream()
                                .filter(p -> p.projectId == project.projectId)
                                .findFirst()
                                .get();
                        long days = DateHelper.getDaysWhenTwoDatesOverlap(thisProject.from, thisProject.to, project.from, project.to);
                        long totalDays = days + this.colleagueWorkingDays.get(empId);
                        this.colleagueWorkingDays.replace(empId, totalDays);
                    });
        }
    }

    Map.Entry<Integer, Long> getBestColleague() {
        int bestColleague = -1;
        long days = 0;

        for (Map.Entry<Integer, Long> colleagueWorkDays : this.colleagueWorkingDays.entrySet()) {
            if (days < colleagueWorkDays.getValue()) {
                days = colleagueWorkDays.getValue();
                bestColleague = colleagueWorkDays.getKey();
            }
        }

        return new AbstractMap.SimpleEntry<Integer, Long>(bestColleague, days);
    }
}