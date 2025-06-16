import sys

def check_smaller(test_tup1, test_tup2):
    for i in range(len(test_tup1)):
        if test_tup1[i] <= test_tup2[i]:
            return False
    return True

def main():
    test_tup1 = list(map(int, sys.argv[1].split(',')))
    test_tup2 = list(map(int, sys.argv[2].split(',')))
    result = check_smaller(test_tup1, test_tup2)
    print(result)

if __name__ == "__main__":
    main()