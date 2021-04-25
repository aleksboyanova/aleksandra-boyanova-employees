import java.time.LocalDate;

class ProjectInfo {
    int projectId;
    LocalDate from;
    LocalDate to;

    ProjectInfo(int projectId, LocalDate from, LocalDate to) {
        this.projectId = projectId;
        this.from = from;
        this.to = to;
    }
}