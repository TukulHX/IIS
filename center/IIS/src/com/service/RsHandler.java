package com.service;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RsHandler<T> {
	T handle(ResultSet rs) throws SQLException;
}
