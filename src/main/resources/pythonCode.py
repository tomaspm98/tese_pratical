import sys

def divide_integers(dividend, divisor):
    if divisor == 0:
        raise ZeroDivisionError("Cannot divide by zero")
    return dividend // divisor

def main():
    if len(sys.argv) != 3:
        raise ValueError("Invalid number of arguments")
    try:
        dividend = int(sys.argv[1])
        divisor = int(sys.argv[2])
        result = divide_integers(dividend, divisor)
        print(result)
    except ValueError as e:
        print(e)

if __name__ == "__main__":
    main()