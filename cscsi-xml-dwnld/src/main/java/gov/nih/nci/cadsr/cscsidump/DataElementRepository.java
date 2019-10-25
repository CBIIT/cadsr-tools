/*
 * Copyright (C) 2019 Frederick National Laboratory for Cancer Research - - All rights reserved.
 */
package gov.nih.nci.cadsr.cscsidump;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
@Repository
public class DataElementRepository {
	private static final Logger logger = LoggerFactory.getLogger(DataElementRepository.class);
    
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Reads CLOB one record, and writes a result to a file.
	 * 
	 * @param fn file path and name to write XML.
	 */
	public void retrieveWriteClob(String fn) {
		final String sqlCsCsiClob = "select TEXT from SBREXT.MDSR_GENERATED_XML where SEQ_ID = (select max(SEQ_ID)from SBREXT.MDSR_GENERATED_XML)";
		BufferedWriter bw = null;
		Reader clobReader = null;
		try {
			logger.debug("retrieveWriteClob: retrieveQueryStr: " + sqlCsCsiClob);
			SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlCsCsiClob);
			if (sqlRowSet.next()) {//we expect just one record
				Object obj = sqlRowSet.getObject(1);
				Clob clobObject = (Clob)obj;
				clobReader = clobObject.getCharacterStream();
				final BufferedReader br = new BufferedReader(clobReader);
				bw = new BufferedWriter(new FileWriter(fn));
				int readNum;
				char[] buff = new char[1048576];//1 MB 524288 1/2 MB
				while ((readNum = br.read(buff, 0, buff.length)) != -1) {
					bw.write(buff,0, readNum);
					bw.flush();
				}
			}
			else {
				logger.error("retrieveWriteClob error no data found to write to: " + fn);
				throw new RuntimeException("retrieveWriteClob no data found: " + sqlCsCsiClob);
			}
		} 
		catch (Exception e) {
			logger.error("retrieveWriteClob error on idseq: " + e.getMessage(), e);
			throw new RuntimeException(e);
		}
		finally {
			if (clobReader != null) {
				try {
					clobReader.close();
				} 
				catch (IOException e) {}
			}
			if (bw != null) {
				try {
					bw.close();
				} 
				catch (IOException e) {}
			}
		}
	} 
	/**
	 * 
	 * @param idseq
	 * @param clazz
	 * @param retrieveQueryStr
	 * @param columnName
	 * @return T
	 */
	public <T> T retrieveBlobData(String idseq, Class<T> clazz, String retrieveQueryStr, String columnName) {
		T blobData = null;
		try {
			byte[] dataByteArr = null;
			LobHandler lobHandler = new DefaultLobHandler();
			logger.debug("retrieveBlobData for ID: " + idseq + ", retrieveQueryStr: " + retrieveQueryStr);
			dataByteArr = (byte[]) jdbcTemplate.queryForObject(retrieveQueryStr, new Object[] { idseq },
				new RowMapper<Object>() {
						// queryForObject expects that at least one object is found, otherwise: DataAccessException
						@Override
						public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
							byte[] requestData = lobHandler.getBlobAsBytes(rs, columnName);
							return requestData;
						}
					});
			blobData = readFromJSON(dataByteArr, clazz);
		} 
		catch (Exception e) {
			logger.error("retrieveBlobData error on idseq: " + idseq, e);
			e.printStackTrace();
		}
		return blobData;
	}

    /**
     * Return java generated UUID to upper case.
     * We could use Oracle caDSR function 
     * 
     * @return String
     */
    public String retrieveIdseq( ) {
    	String idseq = java.util.UUID.randomUUID().toString().toUpperCase();
    	return idseq;
    }
    
	/**
	 * 
	 * @param arr shall not be null
	 * @return 
	 * @return ALSData or null on any error
	 */
	protected static <T> T readFromJSON (byte[] arr, Class<T> clazz) {
		T result = null;
		if (arr != null) {
		try {
				ObjectMapper jsonMapper = new ObjectMapper();
				result = jsonMapper.readValue(arr, clazz);
			}
			catch (Exception e) {
				String msg = "readFromJSON: error reading user data: " + e;
				logger.error(msg, e);
				//TODO remove
				e.printStackTrace();
			}
		}
		return result;
	}
    /**
     * @param sql
     * @param type
     * @param <T>
     * @return
     */
    public <T> List<T> getAll( String sql, Class<T> type )
    {

        List<T> allColumns = jdbcTemplate.query(
                sql, new BeanPropertyRowMapper( type )
        );

        return allColumns;
    }
}
