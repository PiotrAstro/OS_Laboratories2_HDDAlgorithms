package HDDSimulator;

import java.util.*;

public class RequestList implements Iterable<Request> {
    private List<Request> list;
    private boolean recentlyChanged;

    public RequestList(int HDDNumberRange, int numberOfNormalReqests, int numberOfNormalRequestsAtStart, int arrivalTimeRange, int NumberOfRealTimeRequests, int minimumDeadline, int maximumDeadline) {
        list = new ArrayList<>();

        Random random = new Random();

        for(int i = 0; i < numberOfNormalRequestsAtStart; i++) {
            addRequest(new Request(0, random.nextInt(HDDNumberRange)));
        }

        for(int i = numberOfNormalRequestsAtStart; i < numberOfNormalReqests; i++) {
            addRequest(new Request(random.nextInt(arrivalTimeRange), random.nextInt(HDDNumberRange)));
        }

        for(int i = 0; i < NumberOfRealTimeRequests; i++) {
            addRequest(new RealTimeRequest(random.nextInt(arrivalTimeRange), random.nextInt(HDDNumberRange), random.nextInt(maximumDeadline - minimumDeadline) + minimumDeadline));
        }

        recentlyChanged = false;
    }

    public RequestList(RequestList copyFrom) {
        list = new ArrayList<>();

        for(Request request : copyFrom) {
            if(request instanceof RealTimeRequest) {
                list.add(new RealTimeRequest((RealTimeRequest) request));
            }
            else {
                list.add(new Request(request));
            }
        }

        recentlyChanged = false;
    }

    public RequestList() {
        list = new ArrayList<>();
        recentlyChanged = false;
    }

    public void addRequest(Request request) {
        list.add(request);
        recentlyChanged = true;
    }

    public void addRealTimeRequest(RealTimeRequest request) {
        list.add(request);
        recentlyChanged = true;
    }

    public void removeRequest(Request request) {
        list.remove(request);
        recentlyChanged = true;
    }

    public Request getRequest(int index) {
        return list.get(index);
    }

    public int getNumberOfRequests() {
        return list.size();
    }

    public int getNumberOfRealTimeRequests() {
        int numberOfRealTimeRequests = 0;
        for(Request request : list) {
            if(request instanceof RealTimeRequest) {
                numberOfRealTimeRequests++;
            }
        }
        return numberOfRealTimeRequests;
    }

    public int getNumberOfNormalRequests() {
        int numberOfNormalRequests = 0;
        for(Request request : list) {
            if(!(request instanceof RealTimeRequest)) {
                numberOfNormalRequests++;
            }
        }
        return numberOfNormalRequests;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void clear() {
        list.clear();
        recentlyChanged = true;
    }

    public Request getClosestToHDDNumber(int HDDNumber) {
        Request closestRequest = null;
        int closestDistance = Integer.MAX_VALUE;
        for(Request request : list) {
            int distance = Math.abs(request.getHDDNumber() - HDDNumber);
            if(distance < closestDistance) {
                closestDistance = distance;
                closestRequest = request;
            }
        }
        return closestRequest;
    }

    public void swithOfChanged() {
        recentlyChanged = false;
    }

    public boolean getRecentlyChanged() {
        return recentlyChanged;
    }

    public void sortRequests(Comparator<Request> comparator) {
        list.sort(comparator);
    }

    public boolean hasRealTimeRequests() {
        for(Request request : list) {
            if(request instanceof RealTimeRequest) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<Request> iterator() {
        return list.iterator();
    }

    public static class ComparatorByRealTime implements Comparator<Request> {
        private Comparator<Request> comparator;
        private Comparator<RealTimeRequest> comparatorForRealTime;

        public ComparatorByRealTime(Comparator<Request> comparator) {
            this.comparator = comparator;
            comparatorForRealTime = null;
        }

        public ComparatorByRealTime(Comparator<Request> comparator, Comparator<RealTimeRequest> comparatorForRealTime) {
            this.comparator = comparator;
            this.comparatorForRealTime = comparatorForRealTime;
        }

        @Override
        public int compare(Request o1, Request o2) {
            if(!(o1 instanceof RealTimeRequest) && !(o2 instanceof RealTimeRequest)) {
                return comparator.compare(o1, o2);
            } else if(o1 instanceof RealTimeRequest && !(o2 instanceof RealTimeRequest)) {
                return -1;
            } else if(!(o1 instanceof RealTimeRequest) && o2 instanceof RealTimeRequest) {
                return 1;
            } else {
                return comparatorForRealTime == null ? comparator.compare(o1, o2) : comparatorForRealTime.compare((RealTimeRequest) o1, (RealTimeRequest) o2);
            }
        }
    }

    public static class ComparatorByArrivalTime implements Comparator<Request> {
        @Override
        public int compare(Request o1, Request o2) {
            return o1.getArrivalTime() - o2.getArrivalTime();
        }
    }

    public static class ComparatorByHDDNumber implements Comparator<Request> {
        @Override
        public int compare(Request o1, Request o2) {
            return o1.getHDDNumber() - o2.getHDDNumber();
        }
    }

    public static class ComparatorByDeadline implements Comparator<RealTimeRequest> {
        @Override
        public int compare(RealTimeRequest o1, RealTimeRequest o2) {
            return 0;
        }
    }
}
