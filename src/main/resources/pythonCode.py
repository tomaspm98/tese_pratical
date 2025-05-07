import sys

def min_two_numbers(a, b):
    return min(int(a), int(b))

def main():
    if len(sys.argv) != 3 or not all(arg.isdigit() for arg in sys.argv[1:]):
        print("Usage: python script.py <number1> <number2>")
        sys.exit(1)
    
    num1, num2 = sys.argv[1], sys.argv[2]
    result = min_two_numbers(num1, num2)
    print(result)

if __name__ == "__main__":
    main()