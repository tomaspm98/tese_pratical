import sys

def is_Even(digit):
    return digit % 2 == 0

def sum_of_digits(num):
    return sum([int(d) for d in str(num)])

def is_Diff(n):
    even_sum = 0
    odd_sum = 0
    for digit in map(int, str(n)):
        if is_Even(digit):
            even_sum += digit
        else:
            odd_sum += digit
    return (even_sum - odd_sum)

def main():
    n = int(sys.argv[1])
    result = is_Diff(n)
    print(result)

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python script.py <number>")
    else:
        main()