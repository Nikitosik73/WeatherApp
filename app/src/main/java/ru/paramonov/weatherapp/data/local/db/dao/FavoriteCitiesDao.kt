package ru.paramonov.weatherapp.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.paramonov.weatherapp.data.local.model.CityDbModel

@Dao
interface FavoriteCitiesDao {

    @Query("select * from favorite_cities")
    fun getAllFavoriteCities(): Flow<List<CityDbModel>>

    @Query("select exists (select * from favorite_cities where id=:cityId limit 1)")
    fun observeIsFavorite(cityId: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToFavorite(city: CityDbModel)

    @Query("delete from favorite_cities where id=:cityId")
    suspend fun removeFromFavorite(cityId: Int)
}