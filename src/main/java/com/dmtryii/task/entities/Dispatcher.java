package com.dmtryii.task.entities;

import com.dmtryii.task.enums.Direction;
import com.dmtryii.task.utils.Generator;

import java.util.Arrays;
import java.util.Collections;

public class Dispatcher {
    private static int[][] arr;
    private int currentFloor = 1;
    private boolean button; // up = true, down = false
    private static int floorQuantity;
    private int passQuantity;
    private int peopleNeedUp;
    private int peopleNeedDown;
    private int freePlace;
    private int[] temp;

    private Generator random;
    private Elevator elevator;
    private Passenger passenger;

    public Dispatcher() {}

    public void init() {
        random = new Generator();
        elevator = new Elevator();
        passenger = new Passenger();

        floorQuantity = random.range(20, 5);
        passQuantity = random.range(10, 0);
        arr = new int[floorQuantity][passQuantity];
        setArr();

        System.out.println("The height of building is " + floorQuantity + " floors\n");
        System.out.println("Its " + passQuantity + " people on the every floor\n");
        show();

        int i = 1;
        do {
            System.out.println("\n>>> Step #" + i + "\n");

            boarding();
            moving();
            exit();
            show();
            i++;
        } while (getZeros(arr) != passQuantity * floorQuantity);
        System.out.println("\n>>>There are no people in the building!");
        System.exit(1);
    }

    private void setArr() {
        for (int i = arr.length - 1, n = arr.length; i >= 0; i--, n--) {
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j] = passenger.getNeededFloor();
                Arrays.stream(arr[i]).distinct();
                if (arr[i][j] == n) {
                    if (n <= floorQuantity / 2) {
                        arr[i][j] = random.range(floorQuantity, n + 1);
                    } else {
                        arr[i][j] = random.range(n-1, 1);
                    }
                }
            }
        }
    }

    private void show() {
        for (int i = arr.length - 1, n = arr.length; i >= 0; i--, n--) {
            System.out.print(" floor" + n + "--");
            for (int j = 0; j < arr[i].length; j++) {
                System.out.printf("%3d", arr[i][j]);
            }
            System.out.print(" | ");
            if (elevator.getDirection() == Direction.UP && getCurrentFloor() == n) {
                System.out.print("^");
                for (int b = 0; b < elevator.getMaxCapacity(); b++) {
                    System.out.printf("%3d", temp[b]);
                }
            } else if (elevator.getDirection() == Direction.DOWN && getCurrentFloor() == n) {
                System.out.print("V");
                for (int b = 0; b < elevator.getMaxCapacity(); b++) {
                    System.out.printf("%3d", temp[b]);
                }
            }
            System.out.println();
        }
    }

    public int getPeopleNeedUp(int floor) {
        int needUp = 0;
        for (int j = 0; j < arr[floor - 1].length; j++) {
            if (arr[floor - 1][j] >= floor && arr[floor - 1][j] > 0) {
                needUp++;
            }
        }
        return needUp;
    }

    public int getPeopleNeedDown(int floor) {
        int needDown = 0;
        for (int j = 0; j < arr[floor - 1].length; j++) {
            if (arr[floor - 1][j] < floor && arr[floor - 1][j] > 0) {
                needDown++;
            }
        }
        return needDown;
    }

    private void boarding() {
        if (getCurrentFloor() == 1) {
            elevator.setDirection(Direction.UP);
        } else if (getCurrentFloor() == floorQuantity) {
            elevator.setDirection(Direction.DOWN);
        }

        if (passQuantity == 0) { // there are no people
            elevator.setDirection(Direction.WAITING);
            System.out.println("\n ----Elevator is waiting now----\n");
            System.exit(1);
        } else if (passQuantity > 0) {
            peopleNeedUp = getPeopleNeedUp(getCurrentFloor());
            peopleNeedDown = getPeopleNeedDown(getCurrentFloor());
            enter();
        }
    }


    private void enter() {
        int[] elevatorArr = new int[elevator.getMaxCapacity()];
        int[] tempUp = new int[passQuantity];
        int[] tempDown = new int[passQuantity];

        getFreePlace();

        if (peopleNeedUp > 0 && elevator.getDirection() == Direction.UP
                || elevator.getDirection() == Direction.WAITING
                && peopleNeedUp > 0) {
            for (int j = 0; j < passQuantity; j++) {
                if (arr[getCurrentFloor() - 1][j] >= getCurrentFloor() && arr[getCurrentFloor() - 1][j] > 0) {
                    tempUp[j] = arr[getCurrentFloor() - 1][j];
                }
            }
            tempUp = reverseSort(tempUp);

            if (peopleNeedUp >= freePlace) {
                for (int j = 0; j < freePlace; j++) {
                    if (tempUp[j] > 0) {
                        elevatorArr[j] = tempUp[j];
                    }
                }
                elevator.setCurrentCapacity(elevator.getMaxCapacity());
            }

            if (peopleNeedUp < freePlace) {
                for (int j = 0; j < peopleNeedUp; j++) {
                    if (tempUp[j] > 0) {
                        elevatorArr[j] = tempUp[j];
                    }
                }
                elevator.setCurrentCapacity(elevator.getCurrentCapacity() + peopleNeedUp);
            }

        } else if (peopleNeedDown > 0 && elevator.getDirection() == Direction.DOWN
                || elevator.getDirection() == Direction.WAITING
                && peopleNeedDown > 0) {
            for (int j = 0; j < passQuantity; j++) {
                if (arr[getCurrentFloor() - 1][j] < getCurrentFloor() && arr[getCurrentFloor() - 1][j] > 0) {
                    tempDown[j] = arr[getCurrentFloor() - 1][j];
                }
            }
            tempDown = reverseSort(tempDown);

            if (peopleNeedDown >= freePlace) {
                for (int j = 0; j < freePlace; j++) {
                    if (tempDown[j] > 0) {
                        elevatorArr[j] = tempDown[j];
                    }
                }
                elevator.setCurrentCapacity(elevator.getMaxCapacity());
            }

            if (peopleNeedDown < freePlace) {
                for (int j = 0; j < peopleNeedDown; j++) {
                    if (tempDown[j] > 0) {
                        elevatorArr[j] = tempDown[j];
                    }
                }
                elevator.setCurrentCapacity(elevator.getCurrentCapacity() + peopleNeedDown);
            }
        }

        for (int j = 0; j < elevator.getMaxCapacity(); j++) {
            for (int i = 0; i < arr[getCurrentFloor() - 1].length; i++) {
                if (arr[getCurrentFloor() - 1][i] != 0 && arr[getCurrentFloor() - 1][i] == elevatorArr[j]) {
                    arr[getCurrentFloor() - 1][i] = 0;
                    break;
                }
            }
        }
        elevator.addToComingPeople(elevatorArr);
        temp = elevator.getComingPeople();
    }

    public int findMinNeeded() {
        int minNeededFloor = floorQuantity +1;
        for (int j = 0; j < elevator.getComingPeople().length; j++) {
            if (elevator.getComingPeople()[j] != 0) {
                minNeededFloor = Math.min(minNeededFloor, elevator.getComingPeople()[j]);
            }
        }
        return minNeededFloor;
    }

    public int findMaxNeededFloor() {
        int maxNeededFloor = -1;
        for (int j = 0; j < elevator.getComingPeople().length; j++) {
            if (elevator.getComingPeople()[j] != 0) {
                maxNeededFloor = Math.max(maxNeededFloor, elevator.getComingPeople()[j]);
            }
        }
        return maxNeededFloor;
    }

    public void getFreePlace() {
        freePlace = elevator.getMaxCapacity() - elevator.getCurrentCapacity();
    }

    private void moving() {

        // selection a floor for stopping
        int max = findMaxNeededFloor();
        int min = findMinNeeded();
        int floorUp = getCurrentFloor() + 1;
        int floorDown = getCurrentFloor() - 1;
        getFreePlace();

        // if elevator is moving up
        if (elevator.getDirection() == Direction.UP) {
            if (freePlace == 0) {
                setCurrentFloor(min);
            } else {
                int nextFloor = 0;
                for (int i = floorUp, k = floorQuantity; i <= floorQuantity; i++, k--) {
                    if (getButton(i)) {
                        nextFloor = i;
                        break;
                    } else {
                        if (min != floorQuantity +1) {
                            nextFloor = min;
                            break;
                        } else {
                            if (!getButton(k)) {
                                nextFloor = k;
                                break;
                            } else {
                                elevator.setDirection(Direction.DOWN);
                            }
                        }
                    }
                }
                setCurrentFloor(nextFloor);
            }
            // if elevator is moving down
        } else if (elevator.getDirection() == Direction.DOWN) {
            if (freePlace == 0) {
                setCurrentFloor(max);
            } else {
                int nextFloor = 0;
                for (int k = floorDown, i = 1; k >= 1; k--, i++) {
                    if (!getButton(k)) {
                        nextFloor = k;
                        break;
                    } else {
                        if (max != -1) {
                            nextFloor = max;
                            break;
                        } else {
                            if (getButton(i)) {
                                nextFloor = i;
                                break;
                            } else {
                                elevator.setDirection(Direction.UP);
                                nextFloor = 1;
                            }
                        }
                    }
                }
                setCurrentFloor(nextFloor);
            }
        }
    }

    private void exit() {
        int count = 0;
        for (int j = 0; j < elevator.getMaxCapacity(); j++) {
            if (elevator.getComingPeople()[j] == getCurrentFloor()) {
                count++;
                elevator.getComingPeople()[j] = 0;
            }
        }
        elevator.setCurrentCapacity(elevator.getCurrentCapacity() - count);
    }

    public int getZeros(int[][] numbers) {
        int count = 0;
        for (int[] number : numbers) {
            for (int i : number) {
                if (i == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    public static int getFloorQuantity() {
        return floorQuantity;
    }

    public int[] reverseSort(int[] a) {
        a = Arrays.stream(a).boxed()
                .sorted(Collections.reverseOrder())
                .mapToInt(Integer::intValue)
                .toArray();
        return a;
    }

    public int getCurrentFloor() {
        return this.currentFloor;
    }

    public boolean getButton(int floor) {
        if (getPeopleNeedUp(floor) > 0 && elevator.getDirection() == Direction.UP ||
                this.getCurrentFloor() == 1) {
            setButton(true);
        } else if (getPeopleNeedDown(floor) > 0 && elevator.getDirection() == Direction.DOWN ||
                this.getCurrentFloor() == getFloorQuantity()) {
            setButton(false);
        }
        return this.button;
    }


    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public void setButton(boolean button) {
        this.button = button;
    }
}
