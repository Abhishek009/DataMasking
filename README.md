# DataMasking


The primary objective of this data masking project is to anonymize sensitive data in Big Data environments, ensuring privacy and compliance with regulations. This project leverages Scala, Apache Spark, and the Java-FPE library to perform data masking efficiently across large datasets.
## Technologies:

### Scala: 
A powerful, type-safe programming language that provides both object-oriented and functional programming features. It is used to write the core logic of the data masking project.

### Apache Spark: 
An open-source unified analytics engine for large-scale data processing. Spark provides an interface for programming entire clusters with implicit data parallelism and fault tolerance.

### Java-FPE (Format-Preserving Encryption): 
A library that provides format-preserving encryption, allowing for secure masking of data while maintaining the original format. The project utilizes Java-FPE to implement masking algorithms that do not alter the data structure.

## Core Components and Workflow
### DataFrame Operations with Spark:

**Initialization:** Set up SparkSession and create or load DataFrames representing the datasets to be masked.

**Schema Mapping:** Analyze the schema of the DataFrame to identify columns requiring masking based on metadata properties (e.g., which columns contain PII).

**Prefix Adjustment:** Adjust the length of columns to ensure data consistency before masking. For instance, adding prefixes to make the length at least 6 characters.

### Column Transformation:

**Prefix Addition:** Implement logic to add necessary prefixes (e.g., 0 for integers and X for strings) to ensure columns meet a specific length requirement.

**Data Masking:** Apply the Java-FPE library to perform format-preserving encryption. This step ensures that sensitive data is masked while maintaining its original format and structure.

**UDFs for Masking:**

User-Defined Functions (UDFs): Use UDFs to apply custom masking functions to DataFrame columns. These functions encapsulate the logic of the Java-FPE encryption and any additional transformations required.

### Metadata-Driven Masking:

**Properties Handling:** Use properties files or configurations to determine which columns contain PII and require masking. The properties also specify any custom alphabets or configurations needed for the masking process.

**Dynamic Column Handling:** Iterate through the DataFrame columns, apply the prefix logic and masking UDFs conditionally based on the metadata.

### Masked Data Output:

**Output:** Save or export the masked DataFrame for further processing or analysis, ensuring that sensitive data is anonymized.