import java.util.concurrent.atomic.AtomicInteger;

public class Statistics {
    //variables for statistics:
    //AtomicInteger for a Thread-Safe environment.
    private AtomicInteger totalClients = new AtomicInteger(0);
    private AtomicInteger totalRequests = new AtomicInteger(0);
    private AtomicInteger totalInvalidrequests = new AtomicInteger(0);
    private AtomicInteger resultSum = new AtomicInteger(0);

    private AtomicInteger addCount = new AtomicInteger(0);
    private AtomicInteger subCount = new AtomicInteger(0);
    private AtomicInteger mulCount = new AtomicInteger(0);
    private AtomicInteger divCount = new AtomicInteger(0);

    //Variables for periodic statistics (every 10seconds):
    private AtomicInteger periodRequests = new AtomicInteger(0);
    private AtomicInteger periodInvalidRequests = new AtomicInteger(0);
    private AtomicInteger periodResultSum = new AtomicInteger(0);

    private AtomicInteger periodAddCount = new AtomicInteger(0);
    private AtomicInteger periodSubCount = new AtomicInteger(0);
    private AtomicInteger periodMulCount = new AtomicInteger(0);
    private AtomicInteger periodDivCount = new AtomicInteger(0);


    public void incrementClients(){
        totalClients.incrementAndGet();
    }

    public void incrementRequests(){
        totalRequests.incrementAndGet();
        periodRequests.incrementAndGet();
    }

    public void incrementInvalidRequests(){
        totalInvalidrequests.incrementAndGet();
    }

    public void incrementOperationCount(String operation){
        switch(operation){
            case "ADD" -> {
                addCount.incrementAndGet();
                periodAddCount.incrementAndGet();
            }
            case "SUB" -> {
                subCount.incrementAndGet();
                periodSubCount.incrementAndGet();
            }
            case "MUL" -> {
                mulCount.incrementAndGet();
                periodMulCount.incrementAndGet();
            }
            case "DIV" -> {
                divCount.incrementAndGet();
                periodDivCount.incrementAndGet();
            }
        }
    }

    public void addToResultSum(int result){
        resultSum.addAndGet(result);
        periodResultSum.addAndGet(result);
    }

    public synchronized void printStatistics(){
        System.out.println("Global statistics:");
        System.out.println("Total Client count: " + totalClients.get());
        System.out.println("Total Request count: " + totalRequests.get());
        System.out.println("Total Invalid Requests count: " + totalInvalidrequests.get());
        System.out.println("Operation Conunts:" +
                "\nADD: " + addCount.get() + "\nSUB: " +subCount.get()
                + "\nMUL: " + mulCount + "\nDIV: " + divCount.get());
        System.out.println("Sum of results: " + resultSum.get());

        System.out.println("\n10 second Statistics update:");
        System.out.println("Total Request count: " + periodRequests.get());
        System.out.println("Total Invalid Requests count: " + periodInvalidRequests.get());
        System.out.println("Operation Conunts:" +
                "\nADD: " + periodAddCount.get() + "\nSUB: " +periodSubCount.get()
                + "\nMUL: " + periodMulCount + "\nDIV: " + periodDivCount.get());
        System.out.println("Sum of results: " + periodResultSum.get());

        resetPeriodicStatistics();
    }

    private void resetPeriodicStatistics(){
        periodRequests.set(0);
        periodInvalidRequests.set(0);
        periodResultSum.set(0);
        periodAddCount.set(0);
        periodSubCount.set(0);
        periodMulCount.set(0);
        periodDivCount.set(0);
    }

}
