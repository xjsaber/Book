#pragma once

#define SELECTIONSORT_SORTTESTHELPER_H

#include <iostream>
#include <time.h>
#include <cassert>

using namespace std;

namespace SortTestHelper {

	// ������n��Ԫ�ص�������飬ÿ��Ԫ�ص������ΧΪ[rangeL, rangeR]
	int* generateRandomArray(int n, int rangeL, int rangeR) {
		int* arr = new int[n];

		assert(rangeL <= rangeR);
		srand(time(nullptr));
        for (int i = 0; i < n; i++){
            arr[i] = rand() % (rangeR - rangeL + 1) + rangeL;
        }
		return arr;
	}
}
