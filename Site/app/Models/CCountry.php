<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $C_Country_id
 * @property string     $C_Country_name
 * @property int        $sync_ID
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class CCountry extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'C_Country';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'C_Country_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'C_Country_id', 'C_Country_name', 'sync_ID', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [];

    /**
     * The attributes that should be casted to native types.
     *
     * @var array
     */
    protected $casts = [
        'C_Country_id' => 'int', 'C_Country_name' => 'string', 'sync_ID' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * Indicates if the model should be timestamped.
     *
     * @var boolean
     */
    public $timestamps = false;

    // Scopes...

    // Functions ...

    // Relations ...
    public function eq_lang()
    {
        return $this->hasMany(CLang::class, 'C_Lang_id');
    }
    public function eq_link()
    {
        return $this->hasMany(CLinks::class, 'C_Country_id');
    }

    public function eq_country()
    {
        return $this->hasMany(CCountryLng::class, 'C_Country_id');
    }
}
