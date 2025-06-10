# Chat Viewer
A JavaFX-based desktop application for viewing and displaying chat messages from .msg files with support for emoticons and formatted text display.

## Features
* __File Loading:__ Load chat messages from .msg format files
* __Emoticon Support:__ Automatic conversion of text emoticons :) and :( to images
* __Smart Display:__ Consecutive messages from the same user show "..." instead of repeating the nickname
* __Formatted Text:__ Messages displayed with timestamps, colored nicknames, and bold content
* __Scroll Support:__ Scrollable message display for long conversations
* __Error Handling:__ Comprehensive error handling with user-friendly alerts

## Prerequisites:
* __Operation system__ - Windows, Linux or macOS
* __JDK__ - (Java Development Kit) at least 17
* __Maven__ - Installed Maven and stored on the system path

## Configuration and Execution
1. Install the source code or clone the git repository;
2. In the project root, launch the terminal or command prompt;
3. Build and run using command: `mvn clean javafx:run`
4. **Click "Load File"** to open a file chooser
5. **Select** a `.msg` **file** containing chat messages
6. **View the formatted messages** in the main window
### The application will:
* Display timestamps in brackets
* Show usernames in blue (or "..." for consecutive messages from the same user)
* Render message content in bold
* Convert `:)` and `:(` to emoticon images (if available)
* Provide scrolling for long conversations

## Testing
Run the unit tests with: `mvn test` command
### The test suite includes:
* Message parsing validation
* Timestamp format verification
* Content validation
* Nickname handling
