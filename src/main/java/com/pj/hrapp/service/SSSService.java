package com.pj.hrapp.service;

import java.util.List;

import com.pj.hrapp.model.SSSContributionTableEntry;

public interface SSSService {

	SSSContributionTableEntry getSSSContributionTableEntry(long id);

	void save(SSSContributionTableEntry entry);

	List<SSSContributionTableEntry> getAllSSSContributionTableEntries();
	
}
