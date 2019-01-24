package com.engine.parser;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

import com.engine.domain.abstractmodel.Block;
import com.engine.domain.abstractmodel.Collection;
import com.engine.domain.abstractmodel.Entry;
import com.engine.domain.abstractmodel.PartitionKey;
import com.engine.domain.abstractmodel.SortKey;
import com.engine.domain.enumeration.Predicate;

@Component
public class CassandraParser implements Parser {

	public final static String KEYSPACE_DECLARATION = "CREATE KEYSPACE encompass WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '3'}  AND durable_writes = true;";

	public final static String CREATE_TABLE = "CREATE TABLE encompass.";

	public final static String PRIMARY_KEY = "	PRIMARY KEY";

	public final static String WITH_CLUSTERING_ORDER_BY = "WITH CLUSTERING ORDER BY";

	public final static String OPTIONS = "	AND bloom_filter_fp_chance = 0.01\n"
			+ "    AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}\n" + "    AND comment = ''\n"
			+ "    AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}\n"
			+ "    AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}\n"
			+ "    AND crc_check_chance = 1.0\n" + "    AND dclocal_read_repair_chance = 0.1\n"
			+ "    AND default_time_to_live = 0\n" + "    AND gc_grace_seconds = 864000\n"
			+ "    AND max_index_interval = 2048\n" + "    AND memtable_flush_period_in_ms = 0\n"
			+ "    AND min_index_interval = 128\n" + "    AND read_repair_chance = 0.0\n"
			+ "    AND speculative_retry = '99PERCENTILE';\n";

	public CassandraParser() {

	}

	@Override
	public String getNameColumnFamily(Collection collection) {
		if (collection.getName() != null)
			return collection.getName().replaceAll(" ", "_").toLowerCase();
		else // collection has no name because was not found a view component leaf
			return "Collection";
	}

	@Override
	public String getSortKeys(Block block) {
		String sortKeys = "";
		if (!block.getKey().getSortKeys().isEmpty())
			sortKeys = block.getKey().getSortKeys().stream()
					.map(sk -> sk.getEntity().toLowerCase() + "." + sk.getName() + " " + sk.getOrdering().toString())
					.collect(Collectors.joining(","));
		return sortKeys;
	}

	@Override
	public String getPrimaryKey(Block block) {
		String partitionKeys = "";
		String sortKeys = "";

		if (!block.getKey().getPartitionKeys().isEmpty())
			partitionKeys = block.getKey().getPartitionKeys().stream()
					.map(pk -> pk.getEntity().toLowerCase() + "." + pk.getName()).collect(Collectors.joining(","));

		if (!block.getKey().getSortKeys().isEmpty())
			sortKeys = block.getKey().getSortKeys().stream()
					.map(sk -> sk.getEntity().toLowerCase() + "." + sk.getName()).collect(Collectors.joining(","));

		return "(" + partitionKeys + "),(" + sortKeys + ")";
	}

	@Override
	public String getColumns(List<Entry> entries) {
		// remove duplicates
		return entries.stream().distinct()
				.map(e -> "	" + e.getEntityName().toLowerCase() + "." + e.getName() + " " + e.getType())
				.collect(Collectors.joining(",\n"));
	}

	/*
	 * returns directly the script to be run on the shell of cassandra :)
	 */
	@Override
	public String buildPhysicalModel(Collection collection) {

		return KEYSPACE_DECLARATION + "\n\n" + CREATE_TABLE + getNameColumnFamily(collection) + "(\n"
				+ getColumns(collection.getBlock().getEntries()) + "\n" + PRIMARY_KEY + "("
				+ getPrimaryKey(collection.getBlock()) + ")\n" + ")" +
				// jump clustering order by if there are no sort keys
				((getSortKeys(collection.getBlock()) == "") ? ""
						: (WITH_CLUSTERING_ORDER_BY + "(" + getSortKeys(collection.getBlock()) + ")\n"))
				+ OPTIONS;
	}

	@Override
	public String getPartitionKeys(List<PartitionKey> partitionKeys) {
		String partitionKeysTemp = "";
		if (!partitionKeys.isEmpty())
			partitionKeysTemp = partitionKeys.stream().map(pk -> "	" + pk.getEntity().toLowerCase() + "."
					+ pk.getName() + " " + (pk.getType() == null ? "string" : pk.getType()))
					.collect(Collectors.joining(",\n"));
		return partitionKeysTemp;
	}

	@Override
	public String getSortKeys(List<SortKey> sortKeys) {
		String sortKeysTemp = "";
		if (!sortKeys.isEmpty())
			sortKeysTemp = sortKeys.stream()
					.map(sk -> "	" + sk.getEntity().toLowerCase() + "." + sk.getName() + " " + sk.getType())
					.collect(Collectors.joining(",\n"));
		return sortKeysTemp;
	}

	@Override
	public String createSelectQuery(Collection collection) {

		// create target list
		String targetList = "";
		if (collection.getBlock().getEntries().isEmpty())
			targetList = "*";
		else
			targetList = collection.getBlock().getEntries().stream()
					.map(e -> e.getEntityName().toLowerCase() + "." + e.getName()).collect(Collectors.joining(", "))
					.toString();

		String columnFamily = collection.getName().toLowerCase().replaceAll(" ", "_");

		String partitionKeyConditions = collection.getBlock().getKey().getPartitionKeys().stream().map(pk -> {

			String partitionKey = pk.getEntity().toLowerCase() + "." + pk.getName() + " " + pk.getPredicate().value();
			if (pk.getValueCondition() == null)
				return partitionKey + " ?";
			else
				return partitionKey + " " + pk.getValueCondition();

		}).collect(Collectors.joining(" AND ")).toString();

		// String sortKeyConditions = "";
		//
		// for (SortKey sk : collection.getBlock().getKey().getSortKeys()) {
		// if (sk.getPredicate() != null) {
		//
		// String sortKey = sk.getEntity().toLowerCase() + "." + sk.getName() + " " +
		// sk.getPredicate().value();
		// if (sk.getValueCondition() == null)
		// sortKeyConditions += sortKey + " ?";
		// else
		// sortKeyConditions += sortKey + sk.getValueCondition();
		//
		// sortKeyConditions += " AND";
		// }
		// }
		// if (sortKeyConditions.substring(sortKeyConditions.lastIndexOf(" ") +
		// 1).equals("AND"))
		// sortKeyConditions = sortKeyConditions.substring(0,
		// sortKeyConditions.lastIndexOf(" ") + 1);

		String entryConditions = "";

		for (Entry e : collection.getBlock().getEntriesConditions()) {
			if (e.getPredicate() != null) {

				String entry = " " + e.getEntityName().toLowerCase() + "." + e.getName() + " "
						+ e.getPredicate().value();
				if (e.getValueCondition() == null)
					entryConditions += entry + " ?";
				else
					entryConditions += entry + e.getValueCondition();

				entryConditions += " AND";
			}
		}
		if (entryConditions.substring(entryConditions.lastIndexOf(" ") + 1).equals("AND"))
			entryConditions = entryConditions.substring(0, entryConditions.lastIndexOf(" ") + 1);

		String allowFiltering = "";
		if (!entryConditions.isEmpty()) {
			for (Entry entry : collection.getBlock().getEntriesConditions() ) {
				//it's not a sort key
				if (entry.getPredicate()!= null && !entry.getPredicate().equals(Predicate.LESS)
						&& !entry.getPredicate().equals(Predicate.LESS_OR_EQUAL)
						&& !entry.getPredicate().equals(Predicate.GREATER)
						&& !entry.getPredicate().equals(Predicate.GREATER_OR_EQUAL))
					allowFiltering = "ALLOW FILTERING";
				
				
				
			}
		}

		
		
		return "SELECT " + targetList + " FROM " + columnFamily + " WHERE " + partitionKeyConditions
		// + ((!sortKeyConditions.isEmpty()) ? (" AND "+ sortKeyConditions ) :
		// (sortKeyConditions))
				+ ((!entryConditions.isEmpty()) ? (" AND " + entryConditions) : (entryConditions)) + allowFiltering;
	}

}
