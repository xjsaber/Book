import math


def binary_search(list, item):
    low = 0
    high = len(list) - 1
    while low <= high:
        mid = math.ceil((low + high) / 2)
        guess = list[mid]
        if guess == item:
            return mid
        elif guess > item:
            high = mid - 1
        else:
            low = mid + 1
    return None

my_list = []
for num in range(0, 128):
    my_list.append(num)
print(binary_search(my_list, 64))
