import sys

def find_kth_element(arr, k):
    return sorted(arr)[k - 1]

def main():
    if len(sys.argv) != 3:
        print("Usage: python script.py <array> <k>")
        sys.exit(1)
    
    arr = list(map(int, sys.argv[1].split(',')))
    k = int(sys.argv[2])
    
    result = find_kth_element(arr, k)
    print(result)

if __name__ == "__main__":
    main()