package in.co.localization.utility;
 
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;
 
public class LoggableStatement
  implements PreparedStatement
{
  private ArrayList<String> parameterValues;
  private String sqlTemplate;
  private PreparedStatement wrappedStatement;
  public LoggableStatement(Connection connection, String sql)
    throws SQLException
  {
    this.wrappedStatement = connection.prepareStatement(sql);
    this.sqlTemplate = sql;
    this.parameterValues = new ArrayList();
  }
  public void addBatch()
    throws SQLException
  {
    this.wrappedStatement.addBatch();
  }
  public void addBatch(String sql)
    throws SQLException
  {
    this.wrappedStatement.addBatch(sql);
  }
  public void cancel()
    throws SQLException
  {
    this.wrappedStatement.cancel();
  }
  public void clearBatch()
    throws SQLException
  {
    this.wrappedStatement.clearBatch();
  }
  public void clearParameters()
    throws SQLException
  {
    this.wrappedStatement.clearParameters();
  }
  public void clearWarnings()
    throws SQLException
  {
    this.wrappedStatement.clearWarnings();
  }
  public void close()
    throws SQLException
  {
    this.wrappedStatement.close();
  }
  public boolean execute()
    throws SQLException
  {
    return this.wrappedStatement.execute();
  }
  public boolean execute(String sql)
    throws SQLException
  {
    return this.wrappedStatement.execute(sql);
  }
  public int[] executeBatch()
    throws SQLException
  {
    return this.wrappedStatement.executeBatch();
  }
  public ResultSet executeQuery()
    throws SQLException
  {
    return this.wrappedStatement.executeQuery();
  }
  public ResultSet executeQuery(String sql)
    throws SQLException
  {
    return this.wrappedStatement.executeQuery(sql);
  }
  public int executeUpdate()
    throws SQLException
  {
    return this.wrappedStatement.executeUpdate();
  }
  public int executeUpdate(String sql)
    throws SQLException
  {
    return this.wrappedStatement.executeUpdate(sql);
  }
  public Connection getConnection()
    throws SQLException
  {
    return this.wrappedStatement.getConnection();
  }
  public int getFetchDirection()
    throws SQLException
  {
    return this.wrappedStatement.getFetchDirection();
  }
  public int getFetchSize()
    throws SQLException
  {
    return this.wrappedStatement.getFetchSize();
  }
  public int getMaxFieldSize()
    throws SQLException
  {
    return this.wrappedStatement.getMaxFieldSize();
  }
  public int getMaxRows()
    throws SQLException
  {
    return this.wrappedStatement.getMaxRows();
  }
  public ResultSetMetaData getMetaData()
    throws SQLException
  {
    return this.wrappedStatement.getMetaData();
  }
  public boolean getMoreResults()
    throws SQLException
  {
    return this.wrappedStatement.getMoreResults();
  }
  public int getQueryTimeout()
		    throws SQLException
		  {
		    return this.wrappedStatement.getQueryTimeout();
		  }
		  public ResultSet getResultSet()
		    throws SQLException
		  {
		    return this.wrappedStatement.getResultSet();
		  }
		  public int getResultSetConcurrency()
		    throws SQLException
		  {
		    return this.wrappedStatement.getResultSetConcurrency();
		  }
		  public int getResultSetType()
		    throws SQLException
		  {
		    return this.wrappedStatement.getResultSetType();
		  }
		  public int getUpdateCount()
		    throws SQLException
		  {
		    return this.wrappedStatement.getUpdateCount();
		  }
		  public SQLWarning getWarnings()
		    throws SQLException
		  {
		    return this.wrappedStatement.getWarnings();
		  }
		  public void setArray(int i, Array x)
		    throws SQLException
		  {
		    this.wrappedStatement.setArray(i, x);
		    saveQueryParamValue(i, x);
		  }
		  public void setAsciiStream(int parameterIndex, InputStream x, int length)
		    throws SQLException
		  {
		    this.wrappedStatement.setAsciiStream(parameterIndex, x, length);
		    saveQueryParamValue(parameterIndex, x);
		  }
		  public void setBigDecimal(int parameterIndex, BigDecimal x)
		    throws SQLException
		  {
		    this.wrappedStatement.setBigDecimal(parameterIndex, x);
		    saveQueryParamValue(parameterIndex, x);
		  }
		  public void setBinaryStream(int parameterIndex, InputStream x, int length)
		    throws SQLException
		  {
		    this.wrappedStatement.setBinaryStream(parameterIndex, x, length);
		    saveQueryParamValue(parameterIndex, x);
		  }
		  public void setBlob(int i, Blob x)
		    throws SQLException
		  {
		    this.wrappedStatement.setBlob(i, x);
		    saveQueryParamValue(i, x);
		  }
		  public void setBoolean(int parameterIndex, boolean x)
		    throws SQLException
		  {
		    this.wrappedStatement.setBoolean(parameterIndex, x);
		    saveQueryParamValue(parameterIndex, new Boolean(x));
		  }
		  public void setByte(int parameterIndex, byte x)
		    throws SQLException
		  {
		    this.wrappedStatement.setByte(parameterIndex, x);
		    saveQueryParamValue(parameterIndex, new Integer(x));
		  }
		  public void setBytes(int parameterIndex, byte[] x)
		    throws SQLException
		  {
		    this.wrappedStatement.setBytes(parameterIndex, x);
		    saveQueryParamValue(parameterIndex, x);
		  }
		  public void setCharacterStream(int parameterIndex, Reader reader, int length)
		    throws SQLException
		  {
		    this.wrappedStatement.setCharacterStream(parameterIndex, reader, length);
		    saveQueryParamValue(parameterIndex, reader);
		  }
		  public void setClob(int i, Clob x)
		    throws SQLException
		  {
		    this.wrappedStatement.setClob(i, x);
		    saveQueryParamValue(i, x);
		  }
		  public void setCursorName(String name)
		    throws SQLException
		  {
		    this.wrappedStatement.setCursorName(name);
		  }
		  public void setDate(int parameterIndex, java.sql.Date x)
		    throws SQLException
		  {
		    this.wrappedStatement.setDate(parameterIndex, x);
		    saveQueryParamValue(parameterIndex, x);
		  }
		  public void setDate(int parameterIndex, java.sql.Date x, Calendar cal)
		    throws SQLException
		  {
		    this.wrappedStatement.setDate(parameterIndex, x, cal);
		    saveQueryParamValue(parameterIndex, x);
		  }
		  public void setDouble(int parameterIndex, double x)
		    throws SQLException
		  {
		    this.wrappedStatement.setDouble(parameterIndex, x);
		    saveQueryParamValue(parameterIndex, new Double(x));
		  }
		  public void setEscapeProcessing(boolean enable)
		    throws SQLException
		  {
		    this.wrappedStatement.setEscapeProcessing(enable);
		  }
		  public void setFetchDirection(int direction)
		    throws SQLException
		  {
		    this.wrappedStatement.setFetchDirection(direction);
		  }
		  public void setFetchSize(int rows)
				    throws SQLException
				  {
				    this.wrappedStatement.setFetchSize(rows);
				  }
				  public void setFloat(int parameterIndex, float x)
				    throws SQLException
				  {
				    this.wrappedStatement.setFloat(parameterIndex, x);
				    saveQueryParamValue(parameterIndex, new Float(x));
				  }
				  public void setInt(int parameterIndex, int x)
				    throws SQLException
				  {
				    this.wrappedStatement.setInt(parameterIndex, x);
				    saveQueryParamValue(parameterIndex, new Integer(x));
				  }
				  public void setLong(int parameterIndex, long x)
				    throws SQLException
				  {
				    this.wrappedStatement.setLong(parameterIndex, x);
				    saveQueryParamValue(parameterIndex, new Long(x));
				  }
				  public void setMaxFieldSize(int max)
				    throws SQLException
				  {
				    this.wrappedStatement.setMaxFieldSize(max);
				  }
				  public void setMaxRows(int max)
				    throws SQLException
				  {
				    this.wrappedStatement.setMaxRows(max);
				  }
				  public void setNull(int parameterIndex, int sqlType)
				    throws SQLException
				  {
				    this.wrappedStatement.setNull(parameterIndex, sqlType);
				    saveQueryParamValue(parameterIndex, null);
				  }
				  public void setNull(int paramIndex, int sqlType, String typeName)
				    throws SQLException
				  {
				    this.wrappedStatement.setNull(paramIndex, sqlType, typeName);
				    saveQueryParamValue(paramIndex, null);
				  }
				  public void setObject(int parameterIndex, Object x)
				    throws SQLException
				  {
				    this.wrappedStatement.setObject(parameterIndex, x);
				    saveQueryParamValue(parameterIndex, x);
				  }
				  public void setObject(int parameterIndex, Object x, int targetSqlType)
				    throws SQLException
				  {
				    this.wrappedStatement.setObject(parameterIndex, x, targetSqlType);
				    saveQueryParamValue(parameterIndex, x);
				  }
				  public void setObject(int parameterIndex, Object x, int targetSqlType, int scale)
				    throws SQLException
				  {
				    this.wrappedStatement.setObject(parameterIndex, x, targetSqlType, scale);
				    saveQueryParamValue(parameterIndex, x);
				  }
				  public void setQueryTimeout(int seconds)
				    throws SQLException
				  {
				    this.wrappedStatement.setQueryTimeout(seconds);
				  }
				  public void setRef(int i, Ref x)
				    throws SQLException
				  {
				    this.wrappedStatement.setRef(i, x);
				    saveQueryParamValue(i, x);
				  }
				  public void setShort(int parameterIndex, short x)
				    throws SQLException
				  {
				    this.wrappedStatement.setShort(parameterIndex, x);
				    saveQueryParamValue(parameterIndex, new Integer(x));
				  }
				  public void setString(int parameterIndex, String x)
				    throws SQLException
				  {
				    this.wrappedStatement.setString(parameterIndex, x);
				    saveQueryParamValue(parameterIndex, x);
				  }
				  public void setTime(int parameterIndex, Time x)
				    throws SQLException
				  {
				    this.wrappedStatement.setTime(parameterIndex, x);
				    saveQueryParamValue(parameterIndex, x);
				  }
				  public void setTime(int parameterIndex, Time x, Calendar cal)
				    throws SQLException
				  {
				    this.wrappedStatement.setTime(parameterIndex, x, cal);
				    saveQueryParamValue(parameterIndex, x);
				  }
				  public void setTimestamp(int parameterIndex, Timestamp x)
				    throws SQLException
				  {
				    this.wrappedStatement.setTimestamp(parameterIndex, x);
				    saveQueryParamValue(parameterIndex, x);
				  }
				  public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
				    throws SQLException
				  {
				    this.wrappedStatement.setTimestamp(parameterIndex, x, cal);
				    saveQueryParamValue(parameterIndex, x);
				  }
				  /**
				   * @deprecated
				   */
				  public void setUnicodeStream(int parameterIndex, InputStream x, int length)
				    throws SQLException
				  {
				    this.wrappedStatement.setUnicodeStream(parameterIndex, x, length);
				    saveQueryParamValue(parameterIndex, x);
				  }
				  public String getQueryString()
				  {
				    StringBuffer buf = new StringBuffer();
				    int qMarkCount = 0;
				    StringTokenizer tok = new StringTokenizer(this.sqlTemplate + " ", "?");
				    while (tok.hasMoreTokens())
				    {
				      String oneChunk = tok.nextToken();
				      buf.append(oneChunk);
				      try
				      {
				        Object value;
				        if (this.parameterValues.size() > 1 + qMarkCount)
				        {
				          value = this.parameterValues.get(1 + qMarkCount++);
				        }
				        else
				        {
				          if (tok.hasMoreTokens()) {
				            value = null;
				          } else {
				            value = "";
				          }
				        }
				        buf.append(value);
				      }
				      catch (Throwable e)
				      {
				        buf.append("ERROR WHEN PRODUCING QUERY STRING FOR LOG." + 
				          e.toString());
				      }
				    }
				    return buf.toString().trim();
				  }
				  private void saveQueryParamValue(int position, Object obj)
				  {
				    String strValue;
				    if (((obj instanceof String)) || ((obj instanceof java.util.Date)))
				    {
				      strValue = "'" + obj + "'";
				    }
				    else
				    {
				      if (obj == null) {
				        strValue = "null";
				      } else {
				        strValue = obj.toString();
				      }
				    }
				    while (position >= this.parameterValues.size()) {
				      this.parameterValues.add(null);
				    }
				    this.parameterValues.set(position, strValue);
				  }
				  public ParameterMetaData getParameterMetaData()
				    throws SQLException
				  {
				    return null;
				  }
				  public void setAsciiStream(int arg0, InputStream arg1)
				    throws SQLException
				  {}
				  public void setAsciiStream(int arg0, InputStream arg1, long arg2)
				    throws SQLException
				  {}
				  public void setBinaryStream(int arg0, InputStream arg1)
				    throws SQLException
				  {}
				  public void setBinaryStream(int arg0, InputStream arg1, long arg2)
				    throws SQLException
				  {}
				  public void setBlob(int arg0, InputStream arg1)
				    throws SQLException
				  {}
				  public void setBlob(int arg0, InputStream arg1, long arg2)
				    throws SQLException
				  {}
				  public void setCharacterStream(int arg0, Reader arg1)
				    throws SQLException
				  {}
				  public void setCharacterStream(int arg0, Reader arg1, long arg2)
				    throws SQLException
				  {}
				  public void setClob(int arg0, Reader arg1)
				    throws SQLException
				  {}
				  public void setClob(int arg0, Reader arg1, long arg2)
				    throws SQLException
				  {}
				  public void setNCharacterStream(int arg0, Reader arg1)
				    throws SQLException
				  {}
				  public void setNCharacterStream(int arg0, Reader arg1, long arg2)
				    throws SQLException
				  {}
				  public void setNClob(int arg0, NClob arg1)
				    throws SQLException
				  {}
				  public void setNClob(int arg0, Reader arg1)
				    throws SQLException
				  {}
				  public void setNClob(int arg0, Reader arg1, long arg2)
				    throws SQLException
				  {}
				  public void setNString(int arg0, String arg1)
				    throws SQLException
				  {}
				  public void setRowId(int arg0, RowId arg1)
				    throws SQLException
				  {}
				  public void setSQLXML(int arg0, SQLXML arg1)
				    throws SQLException
				  {}
				  public void setURL(int arg0, URL arg1)
				    throws SQLException
				  {}
				  public boolean execute(String arg0, int arg1)
				    throws SQLException
				  {
				    return false;
				  }
				  public boolean execute(String arg0, int[] arg1)
				    throws SQLException
				  {
				    return false;
				  }
				  public boolean execute(String arg0, String[] arg1)
				    throws SQLException
				  {
				    return false;
				  }
				  public int executeUpdate(String arg0, int arg1)
				    throws SQLException
				  {
				    return 0;
				  }
				  public int executeUpdate(String arg0, int[] arg1)
				    throws SQLException
				  {
				    return 0;
				  }
				  public int executeUpdate(String arg0, String[] arg1)
				    throws SQLException
				  {
				    return 0;
				  }
				  public ResultSet getGeneratedKeys()
				    throws SQLException
				  {
				    return null;
				  }
				  public boolean getMoreResults(int arg0)
				    throws SQLException
				  {
				    return false;
				  }
				  public int getResultSetHoldability()
				    throws SQLException
				  {
				    return 0;
				  }
				  public boolean isClosed()
				    throws SQLException
				  {
				    return false;
				  }
				  public boolean isPoolable()
				    throws SQLException
				  {
				    return false;
				  }
				  public void setPoolable(boolean arg0)
				    throws SQLException
				  {}
				  public boolean isWrapperFor(Class<?> arg0)
				    throws SQLException
				  {
				    return false;
				  }
				  public <T> T unwrap(Class<T> arg0)
				    throws SQLException
				  {
				    return null;
				  }
				  public void closeOnCompletion()
				    throws SQLException
				  {}
				  public boolean isCloseOnCompletion()
				    throws SQLException
				  {
				    return false;
				  }
				}