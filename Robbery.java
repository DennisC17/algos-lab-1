// You have a heist getaway sack with a capacity, and n items in front of you
// with sizes and worths. Figure out the maximum value you could
// get with the items.

// You are encouraged to make helper functions!
import java.util.Arrays;

public class Robbery {

	public int[][] tableForDP;
	public int xDP;
	public int yDP;

	public void initializeTable(int size, int weight){
		tableForDP = new int[size][weight];
		xDP = size-1;
		yDP = weight-1;

		if(size==weight){
			for(int i=0; i<size; i++){
				tableForDP[i][0] = 0;
				tableForDP[0][i] = 0;
			}
		}
		else if(size<weight){
			for(int i=0; i<size; i++){
				tableForDP[i][0] = 0;
				tableForDP[0][i] = 0;
			}
			for(int i = size; i<weight; i++){
				tableForDP[0][i] = 0;
			}
		}
		else{
			for(int i=0; i<weight; i++){
				tableForDP[i][0] = 0;
				tableForDP[0][i] = 0;
			}
			for(int i = weight; i<size; i++){
				tableForDP[i][0] = 0;
			}
		}
	}

	// Using DP: Get the maximum value with capacity C and n items
	public int maximizeRobWorthRecur(
		int capacity,
		int[] sizes,
		int[] worths
	) {
		// fill in here, change the return
		int returningValue = 0;
		int arrayLength = sizes.length;
		int[] sizesRed, worthsRed;

		// creating reduced arrays without the last element
		if(arrayLength>0){
			sizesRed = Arrays.copyOfRange(sizes, 0, arrayLength-1);
			worthsRed = Arrays.copyOfRange(worths, 0, arrayLength-1);

			// if there is no more items OR space, we cannot add the item
			if(arrayLength == 0 || capacity == 0){
				returningValue = 0;
			}
			// if the weight of the next item exceeds capacity, we cannot add the item
			else if(sizes[arrayLength-1] > capacity){
				returningValue = maximizeRobWorthRecur(capacity, sizesRed, worthsRed);
			}
			else{
				// do not take an item
				int leaveItem = maximizeRobWorthRecur(capacity, sizesRed, worthsRed);
				// take an item, reduce capacity
				int takeItem = worths[arrayLength-1] + maximizeRobWorthRecur(capacity - sizes[arrayLength-1], sizesRed, worthsRed);
				returningValue = Math.max(leaveItem, takeItem);
			}
		}

		return returningValue;
	}

	public int maximizeRobWorthBottomUp(
		int capacity,
		int[] sizes,
		int[] worths
	) {


		// fill in here, change the return
		for(int i = 0; i < sizes.length; i++){
			for(int j = 1; j<= capacity; j++){
				if(sizes[i] > j){
					tableForDP[i+1][j] = tableForDP[i][j];
				}
				else{
					tableForDP[i+1][j] = Math.max( tableForDP[i][j] , tableForDP[i][j-sizes[i]] + worths[i]);
				}
			}
		}

		return tableForDP[sizes.length][capacity];
	}

/**
* Bonus: figure out which items exactly
* Takes in a DP DPTable
* Returns an int array of the individual worths of the items you took
*/
	public int[] takeRobInventory() {
		// fill in here, change the return
		int i = xDP;
		int j = yDP;
		int[] itemValues = new int[i];
		int numberOfItems = 0;

		while(tableForDP[i][j]>0){

			if(tableForDP[i][j] == tableForDP[i-1][j]){
				i--;
			}
			else if(tableForDP[i][j] == tableForDP[i][j-1]){
				j--;
			}
			else{
				itemValues[numberOfItems] = tableForDP[i][j] - tableForDP[i-1][j];
				i--;
				numberOfItems++;
			}
		}

		itemValues = Arrays.copyOfRange(itemValues, 0, numberOfItems);

		return itemValues;
	}

	public static void main(String[] args) {
		Robbery r = new Robbery();
		int bagCapacity = 40;
		int[] itemSizes = {2, 25, 6, 13, 1, 15, 8, 5, 17, 4};
		int[] itemWorths = {35, 120, 900, 344, 29, 64, 67, 95, 33, 10};

		r.initializeTable(itemSizes.length+1, bagCapacity+1);

//		System.out.println();

		int maxWorthRecur = r.maximizeRobWorthRecur(bagCapacity, itemSizes, itemWorths);
		System.out.println("Max worth of the bag: " + maxWorthRecur);
		int maxWorthBottomUp = r.maximizeRobWorthBottomUp(bagCapacity, itemSizes, itemWorths);
		System.out.println("Max worth of the bag: " + maxWorthBottomUp);

		// Bonus: Fill in the helper method takeRobInventory that could help you
		//figure out which items go into the bag that make it max worth. Feel free
		//to change up the parameters and returned types + values of the helper
		// methods above.


		 int[] itemsPicked = r.takeRobInventory();
		 System.out.println("The items we take are worth:");
		 for (int i = 0; i < itemsPicked.length; i++) {
			 System.out.print(itemsPicked[i] + " ");
		 }


	}
}
