<?php

namespace App\Models;

// use App\CCountry;
// use App\CLinksLng;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $C_Link_id
 * @property int        $C_LinkT_id
 * @property string     $C_Link_name
 * @property string     $C_Link_URL
 * @property int        $C_Country_id
 * @property int        $C_St_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class CLinks extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'C_Links';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'C_Link_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'C_Link_id', 'C_LinkT_id', 'C_Link_name', 'C_Link_URL', 'C_Country_id', 'C_St_id', 'created_at', 'updated_at', 'deleted_at'
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
        'C_Link_id' => 'int', 'C_LinkT_id' => 'int', 'C_Link_name' => 'string', 'C_Link_URL' => 'string', 'C_Country_id' => 'int', 'C_St_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
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

    public function eq_type()
    {
        return $this->belongsTo(CLinksType::class, 'C_LinkT_id');
    }
    public function eq_country()
    {
        return $this->belongsTo(CCountry::class, 'C_Country_id');
    }
    public function eq_link()
    {
        return $this->hasMany(CLinksLng::class, 'C_Link_id');
    }
}
