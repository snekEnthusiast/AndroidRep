package com.example.plugins

import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*

@Serializable
data class Product(val id:Int, val name:String, val type:String, val used:Boolean)
@Serializable
data class Type(val id:String, val specs:String, val perishable:Boolean, val plimit:Int)
object Products : Table("Products"){
	val id = integer("id").uniqueIndex()
	val name = varchar("name",50)
	val type = varchar("type",50).references(Types.id)
	val used = bool("used")
	override val primaryKey = PrimaryKey(id)
	//foreign key
}
object Types : Table("Types"){
	val id = varchar("id",50).uniqueIndex()
	val specs = varchar("specs",50)
	val perishable = bool("perishable")
	val plimit = integer("plimit")
}

object DatabaseFactory {
	fun init() {
		val driverClassName = "org.sqlite.JDBC"
		val jdbcURL = "jdbc:sqlite:file:./data/data.db"
		val database = Database.connect(jdbcURL, driverClassName)
		transaction(database) {
			SchemaUtils.create(Products)
		}
	}
	suspend fun <T> dbQuery(block: suspend () -> T): T =
		newSuspendedTransaction(Dispatchers.IO) { block() }
}

class FImpl{
	private fun resultRowToProduct(row: ResultRow) = Product(
		id = row[Products.id],
		name = row[Products.name],
		type = row[Products.type],
		used = row[Products.used]
	)
	suspend fun allProducts(): List<Product> = DatabaseFactory.dbQuery {
		Products.selectAll().map(::resultRowToProduct)
	}
	suspend fun addProduct(id: Int, name: String, type: String, used: Boolean) = DatabaseFactory.dbQuery {
		val insertStatement = Products.insert {
			it[Products.id] = id
			it[Products.name] = name
			it[Products.type] = type
			it[Products.used] = used
		}
		insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToProduct)
	}
	suspend fun deleteProduct(id: Int) = DatabaseFactory.dbQuery {
		Products.deleteWhere {Products.id eq(id)}
	}
	suspend fun updateProduct(id: Int, name: String, type: String, used: Boolean) = DatabaseFactory.dbQuery {
		Products.update ({Products.id eq(id)}){
			it[Products.name] = name
			it[Products.type] = type
			it[Products.used] = used
		}
	}
	private fun resultRowToType(row: ResultRow) = Type(
		id = row[Types.id],
		specs = row[Types.specs],
		perishable = row[Types.perishable],
		plimit = row[Types.plimit]
	)
	suspend fun allTypes(): List<Type> = DatabaseFactory.dbQuery {
		Products.selectAll().map(::resultRowToType)
	}
	suspend fun addType(id:String, specs:String, perishable:Boolean, plimit:Int) = DatabaseFactory.dbQuery {
		val insertStatement = Types.insert {
			it[Types.id] = id
			it[Types.specs] = specs
			it[Types.perishable] = perishable
			it[Types.plimit] = plimit
		}
		insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToType)
	}
	suspend fun deleteType(id: String) = DatabaseFactory.dbQuery {
		Types.deleteWhere {Types.id eq(id)}
	}
	suspend fun updateType(id:String, specs:String, perishable:Boolean, plimit:Int) = DatabaseFactory.dbQuery {
		Types.update ({Types.id eq(id)}){
			it[Types.specs] = specs
			it[Types.perishable] = perishable
			it[Types.plimit] = plimit
		}
	}
}