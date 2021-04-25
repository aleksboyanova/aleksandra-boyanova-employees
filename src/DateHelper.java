import java.time.Duration;
import java.time.LocalDate;

class DateHelper {
    /**
     * Check if two date range has overlap.
     *
     * @param startF first date range start date
     * @param endF   first date range end date
     * @param startS second date range start date
     * @param endS   second date range end date
     * @return true if date range has overlap and false if they don't
     */
    static Boolean hasTimeOverlap(LocalDate startF, LocalDate endF, LocalDate startS, LocalDate endS) {
        boolean fc = endF.compareTo(startS) >= 0 && endS.compareTo(endF) >= 0;
        boolean sc = startF.compareTo(endS) <= 0 && endF.compareTo(endS) >= 0;
        boolean tc = startF.compareTo(startS) == 0 && endF.compareTo(endS) == 0;
        boolean frc = startF.compareTo(startS) >= 0 && startF.compareTo(endS) <= 0;

        return (fc || sc || tc || frc);
    }

    /**
     * Calculate days when two dates overlap.
     *
     * @param startF first date range start date
     * @param endF   first date range end date
     * @param startS second date range start date
     * @param endS   second date range end date
     * @return days as long
     */
    static long getDaysWhenTwoDatesOverlap(LocalDate startF, LocalDate endF, LocalDate startS, LocalDate endS) {
        LocalDate start = startF.compareTo(startS) >= 0 ? startF : startS;
        Boolean t = startF.compareTo(endF) >= 0;
        LocalDate end = endF.compareTo(endS) >= 0 ? endS : endF;
        return Duration.between(start.atStartOfDay(), end.atStartOfDay()).toDays();
    }
}