<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $CtsL_id
 * @property int        $Cts_id
 * @property int        $C_Lang_id
 * @property string     $CtsL_value
 * @property string     $CtsL_text
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class CThemeSiteLng extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'C_Theme_Site_Lng';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'CtsL_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'CtsL_id', 'Cts_id', 'C_Lang_id', 'CtsL_value', 'CtsL_text', 'created_at', 'updated_at', 'deleted_at'
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
        'CtsL_id' => 'int', 'Cts_id' => 'int', 'C_Lang_id' => 'int', 'CtsL_value' => 'string', 'CtsL_text' => 'string', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
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

    public function eq_theme()
    {
        return $this->belongsTo(CThemeSite::class, 'Cts_id');
    }
}
