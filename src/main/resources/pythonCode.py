import sys

def calculate_sum_and_average(n):
    if n < 1:
        return (0, 0)
    total_sum = n * (n + 1) // 2
    average = total_sum / n
    return (total_sum, average)

def main():
    if len(sys.argv) != 2:
        print("Usage: python script.py <number>")
        sys.exit(1)
    
    try:
        number = int(sys.argv[1])
        total_sum, average = calculate_sum_and_average(number)
        print(f"Sum: {total_sum}, Average: {average}")
    except ValueError:
        print("Please provide a valid integer.")
        sys.exit(1)

if __name__ == "__main__":
    main()