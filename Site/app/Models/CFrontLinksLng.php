<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $C_FLL_id
 * @property int        $C_FL_id
 * @property int        $C_Lang_id
 * @property string     $C_FLL_value1
 * @property string     $C_FLL_value2
 * @property string     $C_FLL_url
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class CFrontLinksLng extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'C_Front_Links_Lng';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'C_FLL_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'C_FLL_id', 'C_FL_id', 'C_Lang_id', 'C_FLL_value1', 'C_FLL_value2', 'C_FLL_url', 'created_at', 'updated_at', 'deleted_at'
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
        'C_FLL_id' => 'int', 'C_FL_id' => 'int', 'C_Lang_id' => 'int', 'C_FLL_value1' => 'string', 'C_FLL_value2' => 'string', 'C_FLL_url' => 'string', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
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
        return $this->belongsTo(CLang::class, 'C_Lang_id');
    }

    public function eq_flinks()
    {
        return $this->belongsTo(CFrontLinks::class, 'C_FL_id');
    }
}
