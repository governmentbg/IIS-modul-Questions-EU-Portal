<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $C_LinkL_id
 * @property int        $C_Link_id
 * @property int        $C_Lang_id
 * @property string     $C_LinkL_value
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class CLinksLng extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'C_Links_Lng';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'C_LinkL_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'C_LinkL_id', 'C_Link_id', 'C_Lang_id', 'C_LinkL_value', 'created_at', 'updated_at', 'deleted_at'
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
        'C_LinkL_id' => 'int', 'C_Link_id' => 'int', 'C_Lang_id' => 'int', 'C_LinkL_value' => 'string', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
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
    public function eq_link()
    {
        return $this->belongsTo(CLinks::class, 'C_Link_id');
    }

    public function eq_lang()
    {
        return $this->belongsTo(CLang::class, 'C_Lang_id');
    }
}
