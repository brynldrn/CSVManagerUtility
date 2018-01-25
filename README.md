# CSV Manager Utility
This is a personal utility I made for backing up CSV files, clearing the original and restoring it.

# Installation
1. Clone this repository `https://github.com/brynldrn/CSVManagerUtility.git`
2. Open Eclipse or STS
3. Run as Java Application

# Methods
```javascript
backupCSV(String source_file)
```
This method accepts the **full path with file name**. Saves it on your documents folder (TSC Backups) with a UNIX timestamp.

```javascript
clearCSV(String source_file)
```
This method accepts the **full path with file name**. Deletes the original file from given source then recreates it.

```javascript
restoreCSV(String source_file)
```
This method accepts the **full path with file name**. Restores the file from backups folder (TSC Backups) using the latest found backup in there.

# Compatibility
This works on JDK 1.5+
