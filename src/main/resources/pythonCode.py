import sys

def first_odd(nums):
    for num in nums:
        if int(num) % 2 != 0:
            return True, int(num)
    return False, None

def main():
    nums = sys.argv[1].split(',')
    result = first_odd(nums)
    print(result)

if __name__ == "__main__":
    main()