import sys

def find_First_Missing(array, start, end):
    while start <= end:
        mid = (start + end) // 2
        if array[mid] == mid + 1:
            start = mid + 1
        else:
            end = mid - 1
    return start + 1

def main():
    array = list(map(int, sys.argv[1].split(',')))
    missing_number = find_First_Missing(array, 0, len(array) - 1)
    print(missing_number)

if __name__ == "__main__":
    main()