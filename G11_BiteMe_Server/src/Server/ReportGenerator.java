package Server;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The `ReportGenerator` class is responsible for generating monthly reports on
 * a scheduled basis. This class follows the singleton pattern to ensure only
 * one instance is running at any time.
 * 
 * @version 5.0
 * @authors Amal, Adam, Abed, Heba, Bashar, Mohammed
 */
public class ReportGenerator {
	/** Singleton instance of the `ReportGenerator`. */
	private static ReportGenerator instance;
	/** Scheduler for executing tasks on a scheduled basis. */
	private ScheduledExecutorService scheduler;

	/**
	 * Private constructor to prevent instantiation from outside the class. This is
	 * part of the singleton pattern.
	 */
	private ReportGenerator() {
		// Private constructor for singleton pattern
	}

	/**
	 * Returns the singleton instance of the `ReportGenerator` class.
	 * 
	 * @return The singleton instance of `ReportGenerator`.
	 */
	public static ReportGenerator getInstance() {
		if (instance == null) {
			instance = new ReportGenerator();
		}
		return instance;
	}

	/**
	 * Starts the report generation process, scheduling it to run on the 1st day of
	 * each month.
	 * 
	 * This method calculates the delay until the next scheduled run and sets up a
	 * fixed-rate schedule to execute the report generation task.
	 */
	public void startGeneratingReports() {
		System.out.println("Report Generator Up And Running");
		scheduler = Executors.newSingleThreadScheduledExecutor();

		// Calculate the delay until the 1st day of the next month
		long initialDelay = calculateInitialDelay();

		// Schedule the task to run on the 1st day of each month
		scheduler.scheduleAtFixedRate(this::generateReports, initialDelay, 30L * 24L * 60L * 60L * 1000L,
				TimeUnit.MILLISECONDS);
	}

	/**
	 * Calculates the delay until the 1st day of the next month from the current
	 * date and time.
	 * 
	 * @return The delay in milliseconds until the 1st day of the next month.
	 */
	private long calculateInitialDelay() {
		LocalDate now = LocalDate.now();
		LocalDate firstDayNextMonth;

		if (now.getDayOfMonth() > 1) {
			firstDayNextMonth = now.withDayOfMonth(1).plusMonths(1);
		} else {
			firstDayNextMonth = now.withDayOfMonth(1);
		}

		return java.time.Duration.between(LocalDateTime.now(), firstDayNextMonth.atStartOfDay()).toMillis();
	}

	/**
	 * Generates the monthly reports by interacting with the database.
	 * 
	 * This method is executed on the scheduled date (1st day of each month) to
	 * generate reports for the previous month.
	 */
	private void generateReports() {
		try {
			DBControl dbControl = DBControl.getInstance(); // Get the singleton instance of DBControl

			LocalDate now = LocalDate.now();
			YearMonth lastMonth = YearMonth.from(now.minusMonths(1));

			int year = lastMonth.getYear();
			int month = lastMonth.getMonthValue();

			// Generate reports for the previous month
			dbControl.generateMonthlyReports(year, month);

			System.out.println(
					"Generated reports for " + lastMonth.getMonth() + " " + lastMonth.getYear() + " on the 1st day.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stops the report generation process by shutting down the scheduler.
	 * 
	 * This method ensures that the scheduler is properly terminated when no longer
	 * needed.
	 */
	public void stopGeneratingReports() {
		if (scheduler != null && !scheduler.isShutdown()) {
			scheduler.shutdown();
		}
	}
}
