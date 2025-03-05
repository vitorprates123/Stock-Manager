package model;

/**
 * an interface for reading data from an API.
 */
public interface APIReader {
  /**
   * reads data from an API.
   *
   * @return the data read from the API as a string.
   */
  String readData();
}
